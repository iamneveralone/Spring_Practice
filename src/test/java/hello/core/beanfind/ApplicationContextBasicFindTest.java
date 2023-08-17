package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        // System.out.println("memberService = " + memberService);
        // System.out.println("memberService.getClass() = " + memberService.getClass());
        // getClass() : 자신이 속한 클래스의 Class 객체를 반환하는 함수 (현재 참조하고 있는 클래스를 확인할 수 있음)
        // Object 는 모든 클래스의 최상위 클래스이므로 모든 클래스에서 getClass() 메소드 호출 가능

        // memberService 변수 내에 담긴 실제 객체(MemberServiceImpl 타입 객체)가 MemberServiceImpl 타입의 인스턴스인지 비교
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType(){
        // 인터페이스로 조회하면 인터페이스 구현체가 대상이 됨
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회") // 구체 타입으로 조회하면 유연성이 떨어짐
    void findBeanByName2(){
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    // 테스트는 항상 실패 테스트도 만들어야 함
    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX(){
        // MemberService xxxxx = ac.getBean("xxxxx", MemberService.class);
        // 이 예외가 터져야 테스트가 성공하는 것임
        // 오른쪽의 로직을 실행하면 왼쪽의 예외가 터져야 성공
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("xxxxx", MemberService.class));
    }
}
