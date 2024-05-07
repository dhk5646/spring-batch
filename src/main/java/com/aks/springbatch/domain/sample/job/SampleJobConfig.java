package com.aks.springbatch.domain.sample.job;

import com.aks.springbatch.common.constants.JobConstant;
import com.aks.springbatch.common.constants.SampleJobStepConstant;
import com.aks.springbatch.common.incrementer.NowDateRunIdIncrementer;
import com.aks.springbatch.common.listener.SampleJobListener;
import com.aks.springbatch.domain.sample.parameter.SampleJobParameter;
import com.aks.springbatch.domain.sample.step.SampleStepTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final SampleJobListener sampleJobListener;

    private final SampleStepTasklet sampleStepTasklet;

    @Bean(name = JobConstant.SAMPLE_JOB)
    public Job job() {
        return new JobBuilder(JobConstant.SAMPLE_JOB, jobRepository)
                .listener(sampleJobListener)
                .incrementer(new NowDateRunIdIncrementer())
                .start(sampleStepTasklet())
                .build();
    }

    @Bean(name = SampleJobStepConstant.SAMPLE_STEP)
    @JobScope
    public Step sampleStepTasklet() {
        return new StepBuilder(SampleJobStepConstant.SAMPLE_STEP, jobRepository)
                .tasklet(sampleStepTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public SampleJobParameter sampleJobParameter() {
        return new SampleJobParameter();
    }
}
