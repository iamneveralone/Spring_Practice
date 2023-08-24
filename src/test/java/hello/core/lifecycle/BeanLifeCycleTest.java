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
