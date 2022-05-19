package spring.springreview0515.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.springreview0515.domain.Member;
import spring.springreview0515.service.MemberService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @ResponseBody를 포함. 데이터를 xml로 바로 보낼때 사용
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    //회원 조회1
    // 문제접: 회원에 대한 정보만 받고싶은데, 서비스에 있는 엔티티가 전부 노출이됨.
    // 엔티티에 JsonIgnor 어노테이션을 적어줄수도 있겠지만 엔티티에 쓰면 공통적용되어 유지보수 극악
    // 엔티티 변경되면 api응답값도 바뀌어 오류의 원인이 됨
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    // 회원 조회2
    @GetMapping("/api/v2/members")
    public MemberResult memberV2(){
        // ListMember를 ListMemberDto로 변경
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new MemberResult(collect.size(), collect); // list가 아닌 오브젝트로 반환 {}되어, 신규 필드를 바로 넣을 수 있다
    }

    @Data
    @AllArgsConstructor
    static class MemberResult<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name; // name만 반환하기 위한 DTO. json 필드명이 name. 엔티티쪽 필드명 변경해도 여기가 안바뀌어 스펙이 변하지 않
    }


    // requestBody : json data를 멤버로 쫙 바꿔준다.
    // 회원등록1
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 회원등록2
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName()); // 필드명이 바뀌면 setName -> setUsername으로 변경하면 됨

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //회원 수정
    // 수정은 멱등하다. 같은 값을 넣어도 같은 값
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        //update는 변경성 메소드. update method에서 멤버까지 리턴해버리면, 쿼리까지 날리는 꼴이 됨.
        memberService.update(id, request.getName()); // 여기서 업데이트된 멤버를 반환받을 수도 있지만, 이렇게 되면 업데이트 하면서 쿼리까지 날리는 꼴이됨
        Member findMember = memberService.findOne(id); // 여기서 조회를 한다.
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }


    // 수정용
    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    // 이게 DTO. 이렇게 받으면, 여기에 api 스펙관련 로직을 다 정의하면 됨.
    // Member에 해놓는것보다 별도의 DTO 만드는게 좋음.
    // 등록용
    @Data // 어노테이션 뭐라고 붙는지 확인
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }
    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


}
