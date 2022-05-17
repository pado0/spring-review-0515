package spring.springreview0515.domain.item;

import lombok.Getter;
import lombok.Setter;
import spring.springreview0515.domain.Category;
import spring.springreview0515.exception.NotEnoughStockException;

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

    // 엔티티에 비즈니스로직 추가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
