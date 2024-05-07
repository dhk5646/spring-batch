package com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImportJobController {

    private final JobLauncher jobLauncher;

    private final Job sampleJob;

    @PostMapping("/importJob")
    public void run() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerUpdateFile", "files/customer_update.csv")
                .toJobParameters();

        jobLauncher.run(sampleJob, jobParameters);
    }
}
