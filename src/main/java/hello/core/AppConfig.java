package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// AppConfig 는 애플리케이션의 실제 동작에 필요한 "구현 객체를 생성"한다
// AppConfig 는 생성한 객체 인스턴스의 참조(레퍼런스)를 "생성자를 통해서 주입(연결)"해준다
// 객체의 생성과 연결은 AppConfig 가 담당

// ApplicationContext 를 스프링 컨테이너라고 함
// 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정(구성) 정보로 사용함
// 여기서 @Bean 이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록
// -> 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라고 함
// 스프링 빈은 @Bean 이 붙은 메서드명을 스프링 빈의 이름으로 사용함
// 이전에는 개발자가 필요한 객체를 AppConfig 를 사용해서 직접 조회했지만,
// 이제부터는 스프링 컨테이너를 통해서 필요한 스프링 빈(객체)를 찾아야 함
@Configuration // 설정 정보(구성 정보)
public class AppConfig {
    // 리팩터링을 통해 역할과 구현 클래스가 한눈에 들어오도록 수정
    // -> 애플리케이션 전체 구성이 어떻게 되어 있는지 빠르게 파악할 수 있음

    // MemberService 역할
    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    // MemberRepository 역할
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    // OrderService 역할
    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        // return new OrderServiceImpl(memberRepository(), discountPolicy());
        return null;
    }

    // DiscountPolicy 역할
    @Bean
    public DiscountPolicy discountPolicy(){
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
    // 이제 할인 정책을 변경해도, 애플리케이션 구성 역할을 담당하는 AppConfig 만 변경하면 됨
    // 클라이언트 코드인 OrderServiceImpl 를 포함해서 '사용 영역'의 어떤 코드도 변경할 필요 없음
    // '구성 영역'은 당연히 변경됨
}
// @Bean memberService -> new MemoryMemberRepository() 실행
// memberService 빈을 만드를 코드를 보면, memberRepository() 호출
// -> 이 메소드 호출하면 new MemoryMemberRepository() 실행

// @Bean orderService -> new MemoryMemberRepository() 실행
// orderService 빈을 만드는 코드를 보면, memberRepository() 호출
// -> 이 메소드 호출하면 동일하게 new MemoryMemberRepository() 실행

// 결과적으로 각각 다른 2개의 MemoryMemberRepository 가 생성되면서 싱글톤 깨지는 것처럼 보임