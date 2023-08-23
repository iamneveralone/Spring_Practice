package hello.core.order;


import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
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

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}

// @RequiredArgsConstructor : Lombok 이 제공하는 어노테이션
// -> final 키워드가 붙음으로써 필수값이 된 필드를 모아서 생성자를 자동으로 만들어주는 역할
// -> Lombok 이 자바의 '애노테이션 프로세서'라는 기능을 이용해서 컴파일 시점에 생성자 코드를 자동으로 생성해줌
// 즉, 기존에 작성해주었던 아래의 생성자를 만들어주는 것임 (이제는 코드에서 보이진 않지만, 실제 호출 가능)

// @Autowired
// public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//     this.memberRepository = memberRepository;
//     this.discountPolicy = discountPolicy;
// }

// 최근에는 생성자를 딱 1개 두고, @Autowired 를 생략하는 방법을 주로 사용
// 여기에 Lombok 라이브러리의 @RequiredArgsConstructor 함께 사용하면 기능은 다 제공하면서, 코드 깔끔하게 사용 가능

// @Qualifier : 추가 구분자를 붙여주는 방법 (추가적인 방법을 제공하는 것일뿐, 빈 이름을 변경하는 건 아님)
// @Qualifier 로 주입할 때, @Qualifier("mainDiscountPolicy")를 못 찾으면 mainDiscountPolicy 라는 이름의 스프링 빈을 추가로 찾음
// -> But, 경험상 @Qualifier 는 @Qualifier 를 찾는 용도로만 사용하는 것이 명확하고 좋음

// @Qualifier 의 단점 : 모든 코드에 @Qualifier 를 붙여줘야 한다는 점
// @Primary : 우선 순위를 정하는 방법 (@Autowired 시에 여러 빈이 매칭되면 @Primary 가 우선권을 가짐)
// -> @Primary 를 사용하면 @Qualifier 붙일 필요X

// "우선 순위"
// @Primary 는 기본 값처럼 동작하는 것, @Qualifier 는 매우 상세하게 동작
// 스프링은 자동보다는 수동이, 넓은 범위의 선택권보다는 좁은 범위의 선택권이 우선 순위가 높음
// -> @Primary 와 @Qualifier 중에서는 @Qualifier 가 우선순위가 높음