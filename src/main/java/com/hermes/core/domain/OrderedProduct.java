package com.hermes.core.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long sku;
    private String name;
    private Double price;
    private int quantity = 1; // TODO: Should be updated with the ordered quantity

}
