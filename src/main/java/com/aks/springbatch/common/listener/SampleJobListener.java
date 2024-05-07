package com.aks.springbatch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleJobListener implements JobExecutionListener {

    public void beforeJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            //job success
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            //job failure
        }
    }

    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            //job success
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            //job failure
        }
    }
}
