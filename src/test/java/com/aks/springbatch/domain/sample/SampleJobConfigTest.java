package com.aks.springbatch.domain.sample;

import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.ImportJobConfig;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBatchTest
@SpringJUnitConfig(ImportJobConfig.class)
public class SampleJobConfigTest {

//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;
//
//    @Test
//    public void testJob(@Autowired Job sampleJob) throws Exception {
//        this.jobLauncherTestUtils.setJob(sampleJob);
//
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//
//        // Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
//    }
}