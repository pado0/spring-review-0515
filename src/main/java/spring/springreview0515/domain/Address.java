package spring.springreview0515.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // 세터 없이 생성자를 만들자
    public Address(){

    }

    // 생성자로 초기화해야한다. 값타입은 변경불가해야해서, 초기 세팅 후 변경을 못하도록 세터 선언을 지운다.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
