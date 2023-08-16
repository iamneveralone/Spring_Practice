package hello.core.singleton;

public class SingletonService {

    // 자바가 시작될 때 static 영역에 객체 인스턴스를 미리 하나 생성해서 올린다
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance(){
        return instance;
    }

    // private 생성자 (외부에서 생성 불가)
    private SingletonService(){
    }

    public void logic(){
        System.out.println("싱글톤");
    }
}

// 자기 자신을 내부에 private, static 으로 하나 가지게 함
// static 키워드 사용하면 static 영역에 객체가 딱 1개만 만들어져서 올라감
// private 사용했기 때문에 외부에서 instance 라는 멤버변수에 직접 접근 불가 (static 임에도 불구하고!)
// 멤버 변수 instance 에 접근할 수 있는 다른 방법 필요

// instance 객체는 getInstance() 메서드를 통해서만 조회 가능
// 이 메서드 호출하면 항상 같은 인스턴스 반환 (static 으로 선언한 인스턴스를 반환하기 때문)
// static 메서드로 선언한 이유
// -> static 키워드 붙이지 않으면 생성자가 private 이기 때문에 외부에서 SingletonService 객체 생성 불가
// -> 객체 생성이 안 되므로 당연하게도 getInstance() 메서드 또한 사용 불가
// -> static 키워드 사용하면 객체(인스턴스) 생성 없이 해당 메서드 호출 가능

// static 은 애플리케이션 실행 시 다른 변수나 메서드보다 먼저 메모리에 올라감 (실행 시점에 모두 메모리에 올라감)
// 메모리는 stack, heap 이외에도 static 메모리를 저장하는 영역이 따로 존재
// static 영역에 올라간 메모리는 애플리케이션이 종료되기 전까지 계속 메모리에 상주
// static 은 애플리케이션이 실행될 때 메모리에 생성되기에 별도록 사용자가 인스턴스화 할 필요 없이 사용 가능