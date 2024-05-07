package com.aks.springbatch.domain.sample.parameter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Getter
public class SampleJobParameter {

    @Value("#{jobParameters[longValue]}")
    private Long longValue;

    @Value("#{jobParameters[doubleValu]}")
    private Double doubleValue;

    @Value("#{jobParameters[stringValue]}")
    private String stringType;

    @Value("#{jobParameters[dateValue]}")
    private Date dateValue;


}
