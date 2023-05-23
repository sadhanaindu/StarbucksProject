package com.example.starbucksworker;

/* https://docs.spring.io/spring-data/jpa/docs/2.4.6/reference/html/#repositories */

import com.example.starbucksworker.StarbucksOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StarbucksOrderRepository extends JpaRepository<StarbucksOrder, Long> {

    List<StarbucksOrder> findByRegister(String register);
    Optional<StarbucksOrder> findById(Long id);

}


