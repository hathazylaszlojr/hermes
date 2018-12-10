package com.hermes.core.repository;


import com.hermes.core.domain.ClientOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ClientOrder, Long> {

    List<ClientOrder> findAllByTimestampBetween(Long startDate, Long endDate);

}
