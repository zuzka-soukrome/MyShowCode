package com.zuzka.myshowcode.repository;

import com.zuzka.myshowcode.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("select o from Order o where o.createDateTime >= :createDateTime")
    List<Order> findAllWithCreationDateTimeAfter(@Param("createDateTime") LocalDateTime createDateTime);

}
