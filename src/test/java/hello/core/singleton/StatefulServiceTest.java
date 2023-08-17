package hello.core.singleton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        // Thread A : A 사용자 10000원 주문
        statefulService1.order("userA", 10000);
        // Thread B : B 사용자 20000원 주문
        statefulService2.order("userB", 20000);

        // Thread A : A 사용자 주문 금액 조회
        // A 사용자가 주문하고, 가격 조회하려고 하는데, 그 사이에 B 사용자가 주문한 상황이라고 가정
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{

        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }

    }

}
// TestConfig 에 @Configuration 붙이지 않으면 내부의 메소드를 통해 만들어지는 객체들의 싱글톤 보장X
// 하나의 type 당 Bean 으로 등록으로 등록되는 객체는 1개이지만, 이외에는 Bean 이 아닌 일반 객체들로 생성됨
// 즉, statefulService() 가 3번 호출되면, 스프링 빈으로 등록되는 것 1개, 일반 객체로 존재하는 것 2개
// @Configuration 붙이면 하나의 type 당 객체 1개만 생성되고 스프링 빈으로 등록됨

// 최대한 단순하게 설명하기 위해 실제 Thread 는 사용하지 않았음
// Thread A 에서 A 사용자 코드를 호출, Thread B 에서 B 사용자 코드를 호출한다고 가정
// StatefulService 의 price 필드는 공유되는 필드이기 때문에, 특정 클라이언트가 값 변경 가능
// -> A 사용자의 price 는 10000원이 출력되어야 하는데, 20000원이 출력되는 상황 발생
// 실무에서도 이런 경우 종종 발생 -> 공유 필드는 진짜 조심!! -> 꼭 stateless 설계!!

// stateless : 값이 변경될 수 있는 필드를 가지지 않거나, 메서드를 통해 값이 변경되지 않게 하는 것