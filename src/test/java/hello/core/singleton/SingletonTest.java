package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

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

        // 출력 결과
        // memberService1 = hello.core.member.MemberServiceImpl@55fe41ea
        // memberService2 = hello.core.member.MemberServiceImpl@fbd1f6
        // -> 이렇게 되면 JVM 메모리에 계속 객체가 생성되어 올라가게 됨

        // memberService1 != memberService2
        // isEqualTo : 객체가 가진 값이 같은지 비교
        // isNotSameAs : 객체의 주소 값이 같은지 비교
        assertThat(memberService1).isNotSameAs(memberService2);
    }
    // 우리가 이전에 만들었던 스프링을 적용하지 않은 순수한 DI 컨테이너 AppConfig 는 요청이 들어올 때마다 객체를 새로 생성하는 방식
    // -> 메모리 낭비가 심하다는 단점 존재
    // -> 해결방안 : 해당 객체가 딱 1개만 생성되고, 그 이후에는 이 객체가 공유되도록 설계
    // -> 싱글톤(Singleton) 패턴

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest(){
        // getInstance() 메서드는 static 메서드이기 때문에 객체 생성 없이 사용 가능
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        // 출력 결과
        // singletonService1 = hello.core.singleton.SingletonService@2cd76f31
        // singletonService2 = hello.core.singleton.SingletonService@2cd76f31

        assertThat(singletonService1).isSameAs(singletonService2);
    }
    // 스프링 컨테이너는 기본적으로 객체를 싱글톤으로 만들어서 객체를 관리해줌
    // -> 스프링 컨테이너는 싱글톤 패턴이 가진 단점들을 다 해결하면서 객체를 싱글톤으로 관리함

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // 출력 결과
        // memberService1 = hello.core.member.MemberServiceImpl@2320fa6f
        // memberService2 = hello.core.member.MemberServiceImpl@2320fa6f

        assertThat(memberService1).isSameAs(memberService2);
    }
    // 스프링 컨테이너 덕분에 고객의 요청이 올 때마다 객체를 하나씩 계속 생성하는 것이 아니라
    // 이미 만들어진 단 1개의 객체를 공유해서 효율적으로 재사용 가능

    // 스프링의 기본 빈 등록 방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아님
    // 요청 올 때마다 새로운 객체를 생성해서 반환하는 기능도 제공하긴 함
    // 99%는 싱글톤 빈을 사용한다고 생각하면 됨
}
