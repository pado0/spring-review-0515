package spring.springreview0515.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.springreview0515.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // EntityManager / query는 repository에서 관리
@RequiredArgsConstructor // 생성자 롬복으로 만들기
public class MemberRepository {

    private final EntityManager em; // 엔티티 매니저 주입

    // save시에 persist 해주면 된다.
    public void save(Member member){
        em.persist(member);
    }

    // 단일 회원 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 전체 회원 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //이름으로 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",
                        Member.class).setParameter("name", name)
                .getResultList();
    }
}
