package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    public void logic(String id){
        myLogger.log("service id = " + id);
    }
}
// 비즈니스 로직이 있는 서비스 계층에서도 로그를 출력
// 여기서 중요한 점!
// request 스코프를 사용하지 않고 파라미터(uuid, requestURL)로 이 모든 정보를 서비스 계층에 넘긴다면, 파라미터가 많아서 지저분해짐
// 더 문제는 requestURL 같은 웹과 관련된 정보가, 웹과 관련없는 서비스 계층까지 넘어가게 됨
// -> 웹관 관련된 부분은 컨트롤러까지만 사용해야 함
// -> 서비스 계층은 웹 기술에 종속되지 않고, 가급적 순수하게 유지하는 것이 유지보수 관점에서 좋음
// request 스코프의 MyLogger 덕분에 이런 부분을 파라미터로 넘기지 않고, MyLogger 의 멤버 변수에 저장해서 코드와 계층 깔끔하게 유지 가능