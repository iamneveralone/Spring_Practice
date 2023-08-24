package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.*;

public class PrototypeTest {

    @Test
    void prototypeBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean1);

        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        // 종료하려면 종료 메서드를 직접 호출해줘야 함
        // prototypeBean1.destroy();
        // prototypeBean2.destroy();

        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean{

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
// 출력 결과
// PrototypeBean.init
// prototypeBean1 = hello.core.scope.PrototypeTest$PrototypeBean@146dfe6
// find prototypeBean2
// PrototypeBean.init
// prototypeBean2 = hello.core.scope.PrototypeTest$PrototypeBean@4716be8b

// 정리
// 싱글톤 스코프의 빈은 스프링 컨테이너 생성 시점에 초기화 메서드가 실행되지만,
// 프로토타입 스코프의 빈은 스프링 컨테이너에서 빈을 조회할 때 생성되고, 초기화 메서드도 실행됨
// -> 프로토타입 빈을 2번 조회했기 때문에 완전히 다른 스프링 빈이 생성되고, 초기화도 2번 실행된 것을 확인할 수 있음

// 싱글톤 빈은 스프링 컨테이너가 관리하기 때문에 스프링 컨테이너가 종료될 때 @PreDestory 가 붙은 빈의 종료 메서드가 실행되지만,
// 프로토타입 빈은 스프링 컨테이너가 빈 생성과 의존관계 주입 그리고 초기화까지만 관여하고, 더는 관리하지 않음
// -> 따라서 프로토타입 빈은 스프링 컨테이너가 종료될 때 @PreDestroy 같은 종료 메서드가 전혀 실행되지 않음

// "프로토타입 빈 정리"
// 1. 스프링 컨테이너에 요청할 때마다 새로 생성됨
// 2. 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입, 그리고 초기화까지만 관여함
// 3. 종료 메서드가 호출되지 않음
// 4. 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야 함, 종료 메서드에 대한 호출 또한 클라이언트가 직접 해야 함