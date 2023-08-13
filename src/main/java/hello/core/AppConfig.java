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

// AppConfig 는 애플리케이션의 실제 동작에 필요한 "구현 객체를 생성"한다
// AppConfig 는 생성한 객체 인스턴스의 참조(레퍼런스)를 "생성자를 통해서 주입(연결)"해준다
// 객체의 생성과 연결은 AppConfig 가 담당
public class AppConfig {
    // 리팩터링을 통해 역할과 구현 클래스가 한눈에 들어오도록 수정
    // -> 애플리케이션 전체 구성이 어떻게 되어 있는지 빠르게 파악할 수 있음

    // MemberService 역할
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    // MemberRepository 역할
    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    // OrderService 역할
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    // DiscountPolicy 역할
    public DiscountPolicy discountPolicy(){
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
    // 이제 할인 정책을 변경해도, 애플리케이션 구성 역할을 담당하는 AppConfig 만 변경하면 됨
    // 클라이언트 코드인 OrderServiceImpl 를 포함해서 '사용 영역'의 어떤 코드도 변경할 필요 없음
    // '구성 영역'은 당연히 변경됨
}
