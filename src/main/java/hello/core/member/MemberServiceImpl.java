package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService{

    // 더 이상 MemoryMemberRepository 의존하지 않음
    // 단지 MemoryRepository 인터페이스만 의존 (추상화에만 의존) -> DIP 지키고 있음
    private final MemberRepository memberRepository;

    // MemberServiceImpl 입장에서는 생성자를 통해 어떤 구현 객체가 들어올지(주입될지) 알 수 없음
    // MemberServiceImpl 의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부(AppConfig)에서 결정됨
    // MemberServiceImpl 은 이제부터 "의존 관계에 대한 고민"은 외부에 맡기고, "실행에만 집중"하면 됨
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
// @Component 붙임으로써 @ComponentScan 을 통해 MemberServiceImpl 이 스프링 빈으로 등록됨
// 기존의 AppConfig 에서는 @Bean 으로 직접 설정 정보 작성하고, memberService 에 memberRepository 의존 관계 주입한다고 직접 명시했었음
// 그러나, AutoAppConfig 에는 아무 내용도 없음 -> 그저 MemberServiceImpl 이 스프링 빈으로 등록될 뿐!

// 그럼 의존 관계 주입은 어떻게 해야 하는가?
// AutoAppConfig 설정 정보가 없기 때문에 이 클래스 안에서 해결해야 함!
// @Autowired : 자동 의존관계 주입 -> 생성자에 붙여줌
// -> MemberRepository 타입에 맞는 빈을 찾아와서 자동으로 의존 관계 주입해줌
// -> 마치 ac.getBean(MemberRepository.class) 처럼 동작한다고 생각하면 됨