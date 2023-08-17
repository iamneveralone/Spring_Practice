package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        // 테스트 용도의 getMemberRepository() 메서드를 Impl 에 구현헀기 때문에 인자로 Impl.class 넣음
        // 일반적으로는 구체 타입으로 꺼내기 보다는 인터페이스로 꺼내는 것이 좋음
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        // 출력 결과
        // memberService -> memberRepository = hello.core.member.MemoryMemberRepository@37052337
        // orderService -> memberRepository = hello.core.member.MemoryMemberRepository@37052337
        // memberRepository = hello.core.member.MemoryMemberRepository@37052337
        // 즉, 모두 같은 객체 인스턴스가 공유되어 사용됨

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }
    // 코드 그대로만 보면 예상대로라면
    // call AppConfig.memberService
    // call AppConfig.memberRepository
    // call AppConfig.memberRepository
    // call AppConfig.orderService
    // call AppConfig.memberRepository
    // 이렇게 출력되어야 할 것 같지만,

    // call AppConfig.memberService
    // call AppConfig.memberRepository
    // call AppConfig.orderService
    // 이렇게만 출력됨

    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class); //AppConfig 도 스프링 빈으로 등록됨
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass()); // AppConfig 의 class 타입 출력

        // 출력 결과
        // bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$68989ba6

        // 순수한 클래스라면 class hello.core.AppConfig 이 출력되어야 하는데, 뒤에 이상한 내용이 붙어 있음
        // -> 내가 만든 클래스가 아니라, 스프링이 CGLIB 라는 바이트 코드 조작 라이브러리를 사용해서
        // AppConfig 클래스를 상속받은 임의의 다른 클래스(AppConfig@CGLIB)를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것!
        // 등록된 빈의 이름은 AppConfig 인데, 객체 인스턴스 타입은 AppConfig@CGLIB 인 상황

    }

}
// AppConfig@CGLIB 예상 코드
    /*@Bean
    public MemberRepository memberRepository(){
        if (memberRepository 가 이미 스프링 컨테이너에 등록되어 있으면?){
            return 스프링 컨테이너에서 찾아서 반환;
        }
        else{ // 스프링 컨테이너에 없으면
            기존 로직을 호출해서 MemberRepository 를 생성하고, 스프링 컨테이너에 등록
            return 반환;
        }
    }*/

// @Bean 이 붙은 메서드마다, 스프링 빈이 존재하면 존재하는 빈을 반환하고,
// 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어짐
// -> 덕분에 싱글톤이 보장되는 것!

// AppConfig 에 @Configuration 붙이지 않고, 테스트 돌리면 class hello.core.AppConfig 출력됨
// 또한,
// call AppConfig.memberService
// call AppConfig.memberRepository
// call AppConfig.memberRepository
// call AppConfig.orderService
// call AppConfig.memberRepository
// 이렇게 출력됨 -> MemberRepository 가 총 3번 호출됨 -> 즉, 싱글톤이 깨짐

// @Bean 만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지는 않음
// -> memberRepository() 처럼 의존 관계 주입이 필요해서 직접 호출할 때, 싱글톤 보장X
// 스프링 설정 정보에는 항상 @Configuration 사용하자