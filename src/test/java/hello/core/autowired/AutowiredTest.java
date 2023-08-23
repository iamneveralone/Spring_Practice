package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption(){
        // TestBean 을 bean 으로 등록
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

        // 출력 결과
        // noBean3 = Optional.empty
        // noBean2 = null
    }

    static class TestBean {

        // Member : 스프링 컨테이너가 관리하는 bean 이 아님 -> 아무것도 없는 걸 집어넣는 상황
        // @Autowired(required = false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안 됨
        @Autowired(required = false)
        public void setNoBean1(Member noBean1){
            System.out.println("noBean1 = " + noBean1);
        }

        // @Nullable : 자동 주입할 대상이 없으면 null 이 입력됨
        @Autowired
        public void setNoBean2(@Nullable Member noBean2){
            System.out.println("noBean2 = " + noBean2);
        }

        // Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력됨
        @Autowired
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println("noBean3 = " + noBean3);
        }

    }
}
// @Nullable, Optional 은 스프링 전반에 걸쳐서 지원됨
// ex) 생성자 자동 주입에서 특정 필드에만 사용해도 됨