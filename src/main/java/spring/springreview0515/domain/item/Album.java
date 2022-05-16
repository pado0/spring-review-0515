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
@DiscriminatorValue("A")
public class Album extends Item {
    private String artist;
    private String etc;

}