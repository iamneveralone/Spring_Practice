package hello.core.singleton;

// 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 '상태를 유지(stateful)'하게 설계하면 X
// -> '무상태(stateless)로 설계해야 함!
// 특정 클라이언트에 의존적(특정 클라이언트를 필요로 함)이거나, 특정 클라이언트가 값을 변경할 수 있는 필드 존재하면 안 됨
// 가급적 읽기만 가능해야 함
// 필드 대신에 자바에서 공유되지 않는, 지역 변수, 파라미터, ThreadLocal 등을 사용해야 함
// 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애 발생 가능!!
public class StatefulService {

    private int price; // 상태를 유지하는 필드

    public void order(String name, int price){
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제!
    }

    public int getPrice(){
        return price;
    }
}
