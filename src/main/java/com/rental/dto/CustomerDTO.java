package com.rental.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Integer customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
}
