package hello.core.order;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void createOrder(){
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);

        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}
// 생성자 주입 사용하면 필드에 final 키워드 사용 가능
// -> 그래서 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아줌
// -> 즉, 누락된 부분을 쉽게 탐지 가능
// (중요) 컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다!

// (참고) 수정자(setter) 주입을 포함한 나머지 주입 방식은 모두 생성자 이후에 호출되기 때문에, 필드에 final 키워드 사용 불가
// -> 오직 생성자 주입 방식만 final 키워드 사용 가능