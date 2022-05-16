package spring.springreview0515.domain.item;

import lombok.Getter;
import lombok.Setter;
import spring.springreview0515.domain.Item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
public class Book extends Item {
    private String author;
    private String isbn;
}
