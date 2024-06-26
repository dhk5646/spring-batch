package com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10;

import com.aks.springbatch.common.constants.JobConstant;
import com.aks.springbatch.common.constants.SampleJobStepConstant;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerAddressUpdate;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerContactUpdate;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerNameUpdate;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean(name = JobConstant.IMPORT_JOB)
    public Job job() throws Exception {
        return new JobBuilder(JobConstant.IMPORT_JOB, jobRepository)
                .start(importCustomerUpdates())
                .build();
    }

    @Bean(name = SampleJobStepConstant.importCustomerUpdates)
    public Step importCustomerUpdates() throws Exception {
        return new StepBuilder(SampleJobStepConstant.importCustomerUpdates, jobRepository)
                .<CustomerUpdate, CustomerUpdate>chunk(100, platformTransactionManager)
                .reader(customerUpdateItemReader(null))
                .processor(customerValidatingItemProcessor(null))
                .writer(customerUpdateItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerUpdate> customerUpdateItemReader(
            @Value("#{jobParameters['customerUpdateFile']}") String inputFile) throws Exception {
        return new FlatFileItemReaderBuilder<CustomerUpdate>()
                .name("customerUpdateItemReader")
                .resource(new ClassPathResource(inputFile))
                .lineTokenizer(customerUpdatesLineTokenizer())
                .fieldSetMapper(customerUpdateFieldSetMapper())
                .build();
    }

    @Bean
    public LineTokenizer customerUpdatesLineTokenizer() throws Exception {
        DelimitedLineTokenizer recordType1 = new DelimitedLineTokenizer();

        recordType1.setNames("recordId", "customerId", "firstName", "middleName", "lastName");

        recordType1.afterPropertiesSet();

        DelimitedLineTokenizer recordType2 = new DelimitedLineTokenizer();

        recordType2.setNames("recordId", "customerId", "address1", "address2", "city", "state", "postalCode");

        recordType2.afterPropertiesSet();

        DelimitedLineTokenizer recordType3 = new DelimitedLineTokenizer();

        recordType3.setNames("recordId", "customerId", "emailAddress", "homePhone", "cellPhone", "workPhone", "notificationPreference");

        recordType3.afterPropertiesSet();

        Map<String, LineTokenizer> tokenizers = new HashMap<>(3);
        tokenizers.put("1*", recordType1);
        tokenizers.put("2*", recordType2);
        tokenizers.put("3*", recordType3);

        PatternMatchingCompositeLineTokenizer lineTokenizer = new PatternMatchingCompositeLineTokenizer();

        lineTokenizer.setTokenizers(tokenizers);

        return lineTokenizer;
    }

    @Bean
    public FieldSetMapper<CustomerUpdate> customerUpdateFieldSetMapper() {
        return fieldSet -> {
            switch (fieldSet.readInt("recordId")) {
                case 1:
                    return new CustomerNameUpdate(
                            fieldSet.readLong("customerId"),
                            fieldSet.readString("firstName"),
                            fieldSet.readString("middleName"),
                            fieldSet.readString("lastName"));
                case 2:
                    return new CustomerAddressUpdate(
                            fieldSet.readLong("customerId"),
                            fieldSet.readString("address1"),
                            fieldSet.readString("address2"),
                            fieldSet.readString("city"),
                            fieldSet.readString("state"),
                            fieldSet.readString("postalCode"));
                case 3:
                    String rawPreference = fieldSet.readString("notificationPreference");

                    Integer notificationPreference = null;
                    if (StringUtils.hasText(rawPreference)) {
                        notificationPreference = Integer.parseInt(rawPreference);
                    }

                    return new CustomerContactUpdate(
                            fieldSet.readLong("customerId"),
                            fieldSet.readString("emailAddress"),
                            fieldSet.readString("homePhone"),
                            fieldSet.readString("cellPhone"),
                            fieldSet.readString("workPhone"),
                            notificationPreference);
                default:
                    throw new IllegalArgumentException(
                            "Invalid recordId: " + fieldSet.readInt("recordId"));
            }
        };
    }

    @Bean
    public ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor(CustomerItemValidator validator) {

        ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor = new ValidatingItemProcessor<>(validator);

        customerValidatingItemProcessor.setFilter(true);

        return customerValidatingItemProcessor;
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerNameUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE CUSTOMER " +
                        "SET FIRST_NAME = COALESCE(:firstName, FIRST_NAME)," +
                        "MIDDLE_NAME = COALESCE(:middleName, MIDDLE_NAME)," +
                        "LAST_NAME = COALESCE(:lastName, LAST_NAME)" +
                        "WHERE CUSTOMER_ID = :customerId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerAddressUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE CUSTOMER " +
                        "SET ADDRESS1 = COALESCE(:address1, ADDRESS1)," +
                        "ADDRESS2 = COALESCE(:address2, ADDRESS2)," +
                        "CITY = COALESCE(:city, CITY)" +
                        "STATE = COALESCE(:state, STATE)" +
                        "POSTAL_CODE = COALESCE(:postalCode, POSTAL_CODE)" +
                        "WHERE CUSTOMER_ID = :customerId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerUpdate> customerContactUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE CUSTOMER " +
                        "SET EMAIL_ADDRESS = COALESCE(:emailAddress, EMAIL_ADDRESS)," +
                        "HOME_PHONE = COALESCE(:homePhone, HOME_PHONE)," +
                        "CELL_PHONE = COALESCE(:cellPhone, CELL_PHONE)" +
                        "WORK_PHONE = COALESCE(:workPhone, WORK_PHONE)" +
                        "NOTIFICATION_PREF = COALESCE(:notificationPreferences, NOTIFICATION_PREF)" +
                        "WHERE CUSTOMER_ID = :customerId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ClassifierCompositeItemWriter<CustomerUpdate> customerUpdateItemWriter() {

        CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(customerNameUpdateItemWriter(null),
                customerAddressUpdateItemWriter(null),
                customerContactUpdateItemWriter(null));

        ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter = new ClassifierCompositeItemWriter<>();

        compositeItemWriter.setClassifier(classifier);

        return compositeItemWriter;
    }
}
