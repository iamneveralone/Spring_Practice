package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
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
    public String logDemo(HttpServletRequest request){
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
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
// -> 앞에서 배운 Provider 사용하자!