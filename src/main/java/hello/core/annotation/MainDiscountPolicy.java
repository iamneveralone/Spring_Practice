package hello.core.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented // @Qualifier 검색해서 붙어있는 애노테이션 복사 & 붙여넣기
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
// 애노테이션에는 상속이라는 개념이 없음
// 이렇게 여러 애노테이션을 모아서 사용하는 기능은 스프링이 지원해주는 기능