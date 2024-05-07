package com.aks.springbatch.domain.sample.temp.spring_batch_document;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleJobController {

    private final JobLauncher jobLauncher;

    private final Job sampleJob;

    @PostMapping("/jobLauncher")
    public void run() throws Exception {
        jobLauncher.run(sampleJob, new JobParameters());
    }
}
