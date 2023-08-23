package hello.core.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void createOrder(){
        OrderServiceImpl orderService = new OrderServiceImpl();
        orderService.createOrder(1L, "itemA", 10000);
    }
    // 현재 OrderServiceImpl 은 @Autowired 생성자 주입 사용 중
    // 위와 같이 코드 작성 시 컴파일 오류 발생 -> Why? 생성자에 필요한 인자를 넣어주지 않았기 때문
    // 이 오류를 통해 "아 맞다! 얘는 memberRepository 랑 discountPolicy 가 필요하지!"라고 인지할 수 있음
    // But, 만약에 수정자(setter) 주입이었으면, null exception 발생 -> 원인 알아내는 과정 까다로움
}