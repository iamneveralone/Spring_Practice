package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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