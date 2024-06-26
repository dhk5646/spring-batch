package com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity;

import org.springframework.util.StringUtils;

public class CustomerNameUpdate extends CustomerUpdate {

    private String firstName;
    private String middleName;
    private String lastName;


    public CustomerNameUpdate(long customerId, String firstName, String middleName, String lastName) {
        super(customerId);
        this.firstName = StringUtils.hasText(firstName) ? firstName : null;
        this.middleName = StringUtils.hasText(middleName) ? middleName : null;
        this.lastName = StringUtils.hasText(lastName) ? lastName : null;
    }
}
