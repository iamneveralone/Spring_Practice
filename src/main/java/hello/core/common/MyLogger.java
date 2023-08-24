package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
@Scope(value = "request")
public class MyLogger {

    private String uuid;
    private String requestURL;

    // requestURL 은 나중에 별도로 세팅하기 위해 setter 만들어줌
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();// 전세계적으로 유니크한 id 생성됨
        System.out.println("[" + uuid + "] request scope bean create:" + this);
    }

    @PreDestroy
    public void close(){
        System.out.println("[" + uuid + "] request scope bean close:" + this);
    }
}
// 로그를 출력하기 위한 MyLogger 클래스
// @Scope(value = "request")를 사용해서 request 스코프로 지정
// -> 이제 이 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸됨

// 이 빈이 생성되는 시점에 자동으로 @PostConstruct 초기화 메서드를 사용해서 uuid 생성해서 저장해둠
// 이 빈은 HTTP 요청 당 하나씩 생성되므로, uuid 를 저장해두면 다른 HTTP 요청과 구분 가능
// 이 빈이 소멸되는 시점에 @PreDestroy 사용해서 종료 메세지 남김
// requestURL 은 이 빈이 생성되는 시점에는 알 수 없으므로, 외부에서 setter 로 입력받음