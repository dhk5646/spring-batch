package com.aks.springbatch.domain.sample.step;

import com.aks.springbatch.domain.sample.parameter.SampleJobParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleStepTasklet implements Tasklet {

    private final SampleJobParameter sampleJobParameter;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("longValue : {}", sampleJobParameter.getLongValue());
        log.info("doubleValue : {}", sampleJobParameter.getDoubleValue());
        log.info("stringValue : {}", sampleJobParameter.getStringType());
        log.info("dateValue : {}", sampleJobParameter.getDateValue());

        return RepeatStatus.FINISHED;
    }
}
