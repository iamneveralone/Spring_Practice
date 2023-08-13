package hello.core.member;

public class MemberServiceImpl implements MemberService{

    // 인터페이스(추상화)에도 의존하고, 구현체에도 의존하는 상황
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
