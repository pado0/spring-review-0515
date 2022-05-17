package spring.springreview0515.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.springreview0515.domain.Member;
import spring.springreview0515.repository.MemberRepository;

import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired // 생성자 하나일 때 생략 가능. 생성자 주입.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    @Transactional //db 변경 or 삭제가 일어나는 함수에 선언. tx.begin() / tx.commit()을 자동수행.
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복회원 검증
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
     }

     // 전체 회원 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 단일 회원 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
