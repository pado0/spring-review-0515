package spring.springreview0515.domain;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    // MEMBER-ORDER 1: 다
    @ManyToOne(fetch = FetchType.LAZY) // 연관관계 주인. 외래키가 있는곳에 지정한다. Order가 멤버의 키를 갖고있는 것이므로 여기다!
    @JoinColumn(name = "MEMBER_ID") // Member쪽 객체에서 조인할 컬럼이름
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL) // cascade도 잡아주기
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // cascade도 잡아주기
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // ENUBTYPE 잡아줘야되는지 확인
    private OrderStatus status;

    // 연관관계 편의메서드 정의하기
    // 주인 / 비주인쪽에 값을 한꺼번에 세팅해주기 위함.
    // 아래 세 개의 메소드는 연관관계가 잡힌 멤버, 오더아이템, 딜리버리에 잡는다.
    // 연관관계 주인이 아닌쪽은 읽기만 가능하다. 연관관계 주인이 아닌쪽에만 셋하고 값이 왜 안바뀌지..? 하는 오류를 방지하기 위해 편의 메소드를 만든다.
    // 값이 안바뀌는 db에 플러쉬된 상태가 아니라서 jpa가 fk를 모르기 때문
    // 1. setMember, 2.addOrderItem, 3.setDelivery
}
