package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
        // 기존에는 ApplicationContext 를 close 할 일이 없었는데, close 를 사용할 경우에는
        // ApplicationContext 대신 ConfigurableApplicationContext 또는 AnnotationConfigApplicationContext 사용!
        // ConfigurableApplicationContext 가 AnnotationConfigApplicationContext 상위 클래스! (부모는 자식을 담을 수 있음)
        // ApplicationContext 는 close 메서드 없기 때문
    }

    @Configuration
    static class LifeCycleConfig{
        @Bean
        public NetworkClient networkClient(){
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
// "초기화, 소멸 인터페이스 단점"
// 1. 이 인터페이스는 스프링 전용 인터페이스임, 해당 코드가 스프링 전용 인터페이스에 의존
// 2. 초기화, 소멸 메서드의 이름 변경할 수 없음
// 3. 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없음
// -> 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법들이고, 지금은 더 나은 방법들 있어서 거의 사용X