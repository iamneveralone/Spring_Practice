package hello.core.member;

public class MemberServiceImpl implements MemberService{

    // 더 이상 MemoryMemberRepository 의존하지 않음
    // 단지 MemoryRepository 인터페이스만 의존 (추상화에만 의존) -> DIP 지키고 있음
    private final MemberRepository memberRepository;

    // MemberServiceImpl 입장에서는 생성자를 통해 어떤 구현 객체가 들어올지(주입될지) 알 수 없음
    // MemberServiceImpl 의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부(AppConfig)에서 결정됨
    // MemberServiceImpl 은 이제부터 "의존 관계에 대한 고민"은 외부에 맡기고, "실행에만 집중"하면 됨
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
}
