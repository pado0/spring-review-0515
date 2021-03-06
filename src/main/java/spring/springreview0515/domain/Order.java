package spring.springreview0515.domain;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // cascade도 잡아주기
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // cascade도 잡아주기
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // ENUBTYPE 잡아줘야되는지 확인
    private OrderStatus status;

    /* 연관관계 편의메서드 정의하기 */
    // 주인 / 비주인쪽에 값을 한꺼번에 세팅해주기 위함.
    // 아래 세 개의 메소드는 연관관계가 잡힌 멤버, 오더아이템, 딜리버리에 잡는다.
    // 연관관계 주인이 아닌쪽은 읽기만 가능하다. 연관관계 주인이 아닌쪽에만 셋하고 값이 왜 안바뀌지..? 하는 오류를 방지하기 위해 편의 메소드를 만든다.
    // 값이 안바뀌는 db에 플러쉬된 상태가 아니라서 jpa가 fk를 모르기 때문
    // 1. setMember, 2.addOrderItem, 3.setDelivery. 양방향 연관관계에서!


    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); // member쪽 orders에도 추가한 주문 넣어주기
    }

    // 왜 이건 Order쪽에 정의하지?
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /* 생성 메서드 : 위 세 메소드를 한방에 호출*/
    // 주문 엔티티 생성시 사용.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /* 비즈니스 로직 */
    // 주문 취소
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL); // 주문 상태를 변경하고
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 주문에 묶인 오더 아이템들을 다 취소한다.
        }
    }

    // 조회로직
    public int getTotalPrice(){ // 전체 주문가격을 조회한다.
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrder().getTotalPrice();
        }
        return totalPrice;
    }

}
