package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();

        System.out.println("myLogger = " + myLogger.getClass());

        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }
}
// 로거가 잘 작동하는지 확인하는 테스트용 컨트롤러
// 여기서 HttpServletRequest 를 통해서 요청 URL 을 받았음
// -> requestURL 값 : http://localhost:8080/log-demo
// 이렇게 받은 requestURL 값을 myLogger 에 저장해둠
// -> myLogger 는 HTTP 요청 당 각각 구분되므로 다른 HTTP 요청 때문에 값이 섞이는 걱정은 하지 않아도 됨
// 컨트롤러에서 controller test 라는 로그를 남김

// (참고) requestURL 을 MyLogger 에 저장하는 부분은 컨트롤러보다는 공통 처리가 가능한 '스프링 인터셉터'나 '서블릿 필터' 같은 곳을 활용하는 것이 좋음
// 여기서는 예제를 단순화하고, 스프링 인터셉터를 학습하지 않은 사람들을 위해 컨트롤러를 사용한 것일뿐!

// 이 상태에서 실행하면 기대와 다르게 애플링케이션 실행 시점에 오류 발생
// Error creating bean with name 'myLogger': Scope 'request' is not active for the
// current thread; consider defining a scoped proxy for this bean if you intend to
// refer to it from a singleton;
// -> 스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성해서 주입이 가능하지만,
//    request 스코프 빈은 아직 생성되지 않음 (이 빈은 실제 고객의 요청이 와야 생성 가능하기 때문!)

// -> 결국, 스프링 컨테이너에게 이 스프링 빈을 달라고 하는 단계를 의존관계 주입 단계가 아니라, 실제 고객 요청이 왔을 때로 미루어야 함
// -> 앞에서 배운 ObjectProvider 사용하자!

// ObjectProvider 덕분에 ObjectProvider.getObject()를 호출하는 시점까지 "request scope 빈의 생성을 지연"할 수 있음
// ObjectProvider.getObject()를 호출하는 시점에는 HTTP 요청이 진행 중이므로 request scope 빈의 생성이 정상적으로 처리됨
// ObjectProvider.getObject()를 LogDemoController, LogDemoService 에서 각각 한 번씩 따로 호출해도 같은 HTTP 요청이면 같은 스프링 빈이 반환됨!

// "MyLogger 클래스의 @Scope 에 proxyMode 옵션 사용"
// System.out.println("myLogger = " + myLogger.getClass());
// 출력 결과
// myLogger = class hello.core.common.MyLogger$$EnhancerBySpringCGLIB$$65ad052e
// -> 스프링에 의해 뭔가 조작된 것을 볼 수 있음
// 처음에는 껍데기뿐인 가짜 MyLogger 를 넣어두고, myLogger 의 기능을 실제 호출하는 시점에 진짜를 찾아서 동작함

// "동작 정리"
// CGLIB 라는 라이브러리로 내 클래스를 상속받은 가짜 프록시 객체를 만들어서 주입
// 이 가짜 프록시 객체는 실제 요청이 오면 그 때 내부에서 실제 빈을 요청하는 위임 로직이 들어있음 (없으면 빈 생성한 후 요청)
// 가짜 프록시 객체는 실제 request scope 와는 관계 없음. 그냥 가짜이고, 내부에 단순한 위임 로직만 있고, 싱글톤처럼 동작함

// "특징 정리"
// 프록시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope 를 사용할 수 있음
// 사실 Provider 를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 미룰 수 있다는 점!!
// 꼭 웹 스코프가 아니어도 프록시는 사용 가능