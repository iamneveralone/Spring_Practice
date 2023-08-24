package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.*;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.count).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.count).isEqualTo(1);
    }

    @Test
    void singletonClientUserPrototype(){
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }

    @Scope("singleton") // 싱글톤 빈은 굳이 애노테이션 안 적어줘도 됨
    static class ClientBean{
        private final PrototypeBean prototypeBean; // 생성 시점에 주입

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic(){
            prototypeBean.addCount();
            int count = prototypeBean.count;
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init " + this); // 객체 자기 자신을 출력
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
// clientBean 이라는 싱글톤 빈이 의존관계 주입을 통해서 프로토타입 빈을 주입 받아서 사용하는 예시
// clientBean 은 싱글톤이므로, 보통 스프링 컨테이너 생성 시점에 함께 생성되고, 의존 관계 주입도 발생
// 1. clientBean 은 의존관계 자동 주입을 사용함. 주입 시점에 스프링 컨테이너에 프로토타입 빈을 요청
// 2. 스프링 컨테이너는 프로토타입 빈을 생성해서 clientBean 에 반환, 프로토타입 빈의 count 필드 값은 0
// 이제 clientBean 은 프로토타입 빈을 내부 필드에 보관 (정확히는 참조값을 보관하는 것)

// 클라이언트 A는 clientBean 을 스프링 컨테이너에 요청해서 받음
// 3. 클라이언트 A는 clientBean.logic()을 호출
// 4. clientBean 은 prototypeBean 의 addCount()를 호출해서 프로토타입 빈의 count 를 증가 -> count 값은 1이 됨

// 클라이언트 B는 clientBean 을 스프링 컨테이너에 요청해서 받음 (싱글톤이므로 항상 같은 clientBean 이 반환됨)
// 여기서 중요한 점! clientBean 이 내부에 가지고 있는 프로토타입 빈은 이미 과거에 주입이 끝난 빈임
// -> 주입 시점에 스프링 컨테이너에 요청해서 프로토타입 빈이 새로 생성된 것이지, 사용할 때마다 새로 생성되는 것이 아님!
// 5. 클라이언트 B는 clientBean.logic()을 호출
// 6. clientBean 은 prototypeBean 의 addCount()를 호출해서 프로토타입 빈의 count 를 증가 -> count 값은 2가 됨

// But, 아마 원하는 것이 이런 상황은 아닐 것임
// -> 프로토타입 빈을 주입 시점에만 새로 생성하는 게 아니라, 사용할 때마다 새로 생성해서 사용하는 것을 원할 것임

// 스프링은 일반적으로 싱글톤 빈을 사용하므로, 싱글톤 빈이 프로토타입 빈을 사용하게 됨
// 그런데 싱글톤 빈은 생성 시점에만 의존관계 주입을 받기 때문에 프로토타입 빈이 새로 생성되기는 하지만,
// 싱글톤 빈과 함께 계속 유지되는 것이 문제인 상황!