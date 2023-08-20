package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration // 설정 정보니까 붙여주어야 함
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
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

// 스프링은 컴포넌트 스캔 시 기본으로 싱글톤으로 빈을 등록함

// @ComponentScan 의 basePackages 옵션 : 탐색할 패키지의 시작 위치 지정
// -> 이 패키지를 포함해서 하위 패키지를 모두 탐색
// basePackages = {"hello.core", "hello.service"} 이렇게 여러 시작 위치 지정 가능
// basePackageClasses 옵션 : 지정한 클래스의 패키지를 탐색 시작 위치로 지정

// 만약 아무것도 지정X : @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치로 지정됨

// 권장 방법 : 패키지 위치 지정하지 않고, 설정 정보 클래스 위치를 프로젝트 최상단에 두기
// -> 최상단 패키지를 포함한 하위 패키지는 모두 자동으로 컴포넌트 스캔의 대상이 됨
// -> 또한, 프로젝트 메인 설정 정보는 프로젝트를 대표하는 정보이므로 프로젝트 시작 루트 위치에 두는 것이 좋다고 생각

// 참고로, 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication 를 프로젝트 시작 루트 위치에 두는 것이 관례
// @SpringBootApplication 는 @ComponentScan 을 포함하고 있음