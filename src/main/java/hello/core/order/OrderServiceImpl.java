package hello.core.order;


import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    // final 은 필드에서 초기화 or 생성자로 초기화 둘 중 하나
    // 이 경우는 생성자로 초기화
    // 인터페이스만 존재 (추상화에만 의존), 구현체에 의존X -> DIP 지키고 있음
    // discountPolicy 는 FixDiscountPolicy 가 들어올지 RateDiscountPolicy 가 들어올지 모름
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // OrderServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어올지(주입될지) 알 수 없음
    // OrderServiceImpl 의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부(AppConfig)에서 결정
    // OrderServiceImpl 은 이제부터 실행에만 집중하면 됨
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    // @Autowired 사용하면 생성자에서 여러 의존 관계도 한 번에 주입 받을 수 있음
    // 생성자가 딱 1개면 @Autowired 생략 가능
    // 그러나, 파라미터 없는 기본 생성자와 같은 다른 생성자가 존재하면 사용하려는 특정 생성자에 @Autowired 붙여줘야 함

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}