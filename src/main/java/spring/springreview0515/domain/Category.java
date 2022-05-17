package spring.springreview0515.domain;

import lombok.Getter;
import lombok.Setter;
import spring.springreview0515.domain.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY) // 여기서 manytomany 처리하는거 보충
    private List<Item> items = new ArrayList<>();

    // 계층구조를 만들기위해 재귀적으로 선언
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 연관관계 편의메서드 추가
}
