package com.hermes.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String email;
    private List<Long> productSkus;

}
