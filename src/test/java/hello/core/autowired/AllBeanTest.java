package hello.core.autowired;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AllBeanTest {

    @Test
    void findAllBean(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(2000);
    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        @Autowired // 생성자 하나여서 @Autowired 생략해도 됨
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
        // 출력 결과
        // policyMap = {fixDiscountPolicy=hello.core.discount.FixDiscountPolicy@b7838a9, rateDiscountPolicy=hello.core.discount.RateDiscountPolicy@696f0212}
        // policies = [hello.core.discount.FixDiscountPolicy@b7838a9, hello.core.discount.RateDiscountPolicy@696f0212]
    }
}
// DiscountService 는 Map 으로 모든 DiscountPolicy 를 주입받음
// -> 이 때, fixDiscountPolicy 와 rateDiscountPolicy 가 주입됨

// Map<String, DiscountPolicy> : map 의 key 에 스프링 빈의 이름을 넣어주고, 그 값으로 DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아줌
// List<DiscountPolicy> : DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아줌

// ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
// -> 스프링 컨테이너를 생성하면서, 해당 컨테이너에 동시에 AutoAppConfig, DiscountService 를 스프링 빈으로 자동 등록