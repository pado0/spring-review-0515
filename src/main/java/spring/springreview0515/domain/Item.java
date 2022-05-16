package spring.springreview0515.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorColumn(name = "DTYPE") // 이 노테이션은 여기에 달아야함
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑주체, 싱글테이블로 생성
public class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // mappedBy 해줘야함
    private List<Category> categories = new ArrayList<>();
}
