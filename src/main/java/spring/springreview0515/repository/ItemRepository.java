package spring.springreview0515.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.springreview0515.domain.item.Item;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ItemRepository {

    private final EntityManager em; // 생성자 초기화를 강제한다.

    @Autowired
    public ItemRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Item item){
        if(item.getId() == null) em.persist(item);
        else em.merge(item);
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
