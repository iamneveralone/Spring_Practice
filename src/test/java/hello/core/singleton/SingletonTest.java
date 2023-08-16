package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();
        // 1. 조회 : 호출할 때마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회 : 호출할 때마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        // 참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // 결과
        // memberService1 = hello.core.member.MemberServiceImpl@55fe41ea
        // memberService2 = hello.core.member.MemberServiceImpl@fbd1f6
        // -> 이렇게 되면 JVM 메모리에 계속 객체가 생성되어 올라가게 됨

        // memberService1 != memberService2
        // isEqualTo : 객체가 가진 값이 같은지 비교
        // isNotSameAs : 객체의 주소 값이 같은지 비교
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
    }
}
// 우리가 이전에 만들었던 스프링을 적용하지 않은 순수한 DI 컨테이너 AppConfig 는 요청이 들어올 때마다 객체를 새로 생성하는 방식
// -> 메모리 낭비가 심하다는 단점 존재
// -> 해결방안 : 해당 객체가 딱 1개만 생성되고, 그 이후에는 이 객체가 공유되도록 설계
// -> 싱글톤(Singleton) 패턴