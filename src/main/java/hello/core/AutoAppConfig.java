package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration // 설정 정보니까 붙여주어야 함
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
// @ComponentScan : @Component 붙은 클래스를 찾아서 다 자동으로 스프링 빈으로 등록시킴
// excludeFilters 옵션을 통해 @Configuration 붙은 클래스는 자동으로 스프링 빈 등록되지 않도록 함
// -> 단지 @Configuration 붙어있는 기존의 AppConfig 클래스가 자동으로 스프링 빈으로 등록되는 것을 막기 위함
// (AppConfig 가 스프링 빈으로 등록되면 자동으로 내부에 @Bean 이 붙은 것들도 스프링 빈으로 등록됨)
// -> @Configuration 내부에 @Component 포함되어 있음 -> @ComponentScan 의 스캔 대상이 됨

// (질문) excludeFilters 옵션을 통해 @Configuration 붙은 클래스들은 스캔 대상에서 제외하기로 했는데, 왜 AutoAppConfig 에는 붙어있는가?
// (정답) AutoAppConfig 는 스캔 제외 대상에 포함되지 않음 (사실, 실무에서는 excludeFilters 옵션 거의 사용X)
// 사실, AutoAppConfig 에서는 @ComponentScan 으로 빈들을 등록하기 때문에 @Configuration 사용하지 않아도 됨
// -> 사용한 이유는 스프링에서 설정 파일들은 관례상 @Configuration 붙이기 때문!

// AutoAppConfig 가 스프링 빈으로 등록되는 이유는 다음과 같음
// 사실, @ComponentScan 기능이 활성화되려면, 그보다 먼저 AutoAppConfig 가 스프링 빈으로 등록이 되어야 함
// @ComponentScan 도 스프링이 제공하는 기능이기 때문에 AutoAppConfig 클래스가 스프링 빈으로 등록되어야 동작함
// 그러면 AutoAppConfig 는 언제 스프링 빈으로 등록되는가?
// -> new AnnotationConfigApplicationContext(AutoAppConfig.class);
// -> 스프링 컨테이너를 생성할 때 넘겨준 클래스 정보는 스프링 빈으로 등록됨