package hello.core.scan;

import hello.core.AutoAppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class AutoAppConfigTest {

    @Test
    void basicScan(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
    // AnnotationConfigApplicationContext 를 사용하는 것은 기존과 동일
    // 설정 정보로 AutoAppConfig 클래스를 넘겨줌

    // 로그에서 다음과 같은 내용 확인 가능
    // Identified candidate component class: file [(생략)\hello\core\member\MemberServiceImpl.class]
    // Creating shared instance of singleton bean 'memberServiceImpl'
    // Autowiring by type from bean name 'memberServiceImpl' via constructor to bean named 'memoryMemberRepository'
}
// @ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록함
// 이 때, 스프링 빈의 기본 이름은 클래스 명을 사용하되 맨 앞글자만 소문자를 사용
// [빈 이름 기본 전략] MemberServiceImpl 클래스 -> memberServiceImpl
// [빈 이름 직접 지정] @Component("memberService2")

// 생성자에 @Autowired 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입해줌
// 이 때, 기본 조회 전략은 타입이 같은 빈을 찾아서 주입
// 생성자에 파라미터가 많아도 다 찾아서 자동으로 주입해줌
