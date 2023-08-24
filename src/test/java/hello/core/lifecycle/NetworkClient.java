package hello.core.lifecycle;

public class NetworkClient {

    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출 url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect(){
        System.out.println("connect: " + url);
    }
    
    public void call(String message){
        System.out.println("call: " + url + " message: " + message);
    }

    // 서비스 종료시 호출
    public void disconnect(){
        System.out.println("close: " + url);
    }

    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
// 생성자 부분을 보면 url 정보 없이 connect 가 호출되는 것을 확인할 수 있음
// 너무 당연한 이야기지만, 객체를 생성하는 단계에는 url 이 없고, 객체를 생성한 다음에
// 외부에서 수정자(setter) 주입을 통해서 setUrl()이 호출되어야 url 이 존재하게 되는 상황

// 스프링 빈은 간단하게 다음과 같은 라이프 사이클을 가짐
// "객체 생성" -> "의존 관계 주입" (생성자 주입은 예외 => 이 경우엔 객체 생성될 때 의존관계 주입)

// 스프링 빈은 객체를 생성하고, 의존 관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료됨
// -> 따라서 초기화 작업은 의존 관계 주입이 모두 완료되고 난 다음에 호출해야 한다.
// (단순히 "객체 생성 = 초기화"가 아님, 객체에 필요한 모든 정보가 설정되는 것을 초기화라고 생각해야 함)
// But, 개발자가 의존 관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까?

// 스프링은 의존 관계 주입이 완료되면 스프링 빈에게 '콜백 메서드'를 통해서 초기화 시점을 알려주는 다양한 기능을 제공
// -> 즉, "너는 의존 관계 주입이 끝났으니까 초기화를 해!"라고 알려줌
// 또한, 스프링은 스프링 컨테이너가 종료되기 직전에 '소멸 콜백'을 준다 -> 안전하게 종료 작업 진행 가능

// "스프링 빈의 이벤트 라이프 사이클" (싱글톤인 경우의 예시)
// 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료
// 이 예시에서는 '의존관계 주입'보다는 '값 주입'에 조금 더 가까움

// (참고) "객체의 생성과 초기화를 분리하자"
// 생성자 : 필수 정보(파라미터)를 받고, 메모리 할당해서 객체 생성하는 책임
// 초기화 : 생성된 값들을 활용해서 외부 커넥션을 연결하는 등 무거운 동작 수행 (초기화 = 객체를 생성하기 위해 필요한 추가 작업)
// -> 생성자 안에서 무거운 초기화 작업을 함께 하는 것보다는 객체 생성 부분과 초기화 부분을 명확히 나누는 것이 유지보수 관점에서 좋음
// (의존 관계 주입은 굳이 따지면 초기화보다는 객체 생성 단계에 포함된 개념이라고 생각하는 게 나을 듯)

// 스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원
// 1. 인터페이스(InitializingBean, DisposableBean)
// 2. 설정 정보에 초기화 메서드, 종료 메서드 지정
// 3. @PostConstruct, @PreDestroy 애노테이션 지원