package com.aks.springbatch.common.incrementer;


import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.LocalDateTime;

public class NowDateRunIdIncrementer implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder()
                .addString("run.id", LocalDateTime.now().toString())
                .toJobParameters();
    }
}
