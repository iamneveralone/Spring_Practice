package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {

    // 클래스와 인터페이스 이름에 .class 를 붙이게 되면 각각 Class<클래스>, Class<인터페이스> 타입을 가지는 객체 반환
    // ex) AppConfig.class 는 Class<AppConfig> 타입을 가지는 객체 반환
    // Class 클래스는 실행 중인 자바 애플리케이션 내 클래스와 인터페이스들을 표현하는 클래스
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상 있으면, 중복 오류가 발생한다")
    void findBeanByParentTypeDuplicate(){
        // DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        // 위의 코드를 한 번 실행해보고, 콘솔에 뜨는 Exception 종류를 assertThrows 에 넣어주자
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상 있으면, 빈 이름을 지정하면 된다")
    void findBeanByParentTypeBeanName(){
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType(){
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllBeanByParentType(){
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object")
    void findAllBeanByObjectType(){
        // Object 타입으로 부르면 Object 클래스를 상속하고 있는 모든 타입의 빈 조회
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
    }

    @Configuration
    static class TestConfig {

        // DiscountPolicy 타입으로 빈을 저장했지만, 실제로는 구현체 인스턴스가 저장된 것과 같은 이치
        // 저장된 객체 인스턴스의 클래스 타입이 Object 클래스를 상속하고 있기 때문에 Object 타입으로 불러올 수 있음
        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy(){
            return new FixDiscountPolicy();
        }
    }
}
// static 을 붙이지 않은 inner class 를 감싸고 있는 outer class 에 종속됨
// 즉, outer class 의 객체를 통해서만 inner class 에 접근 가능
// static inner class 는 outer class 내부에 선언되었지만, outer class 의 객체 생성 유무와 별개로 만들어짐
// 즉, 독립적으로 사용 가능한 것
// Test 클래스 : outer class, TestConfig 클래스 : inner class
// TestConfig 클래스에 static 을 뺀다면 Test 클래스가 생성되어야 TestConfig 사용 가능
// 하지만, Test 클래스 내에서 TestConfig 생성되기도 전에 스프링 컨테이너에서 TestConfig 빈이 있는지 찾아오려고 함
// new AnnotationConfigApplicationContext(TestConfig.class); <- 여기서!!
// static 붙이면 Test 클래스와 별개로 TestConfig 클래스 생성되고, 빈으로 등록됨
// -> Test 클래스에서 TestConfig 불러와서 사용 가능
