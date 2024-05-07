package com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10;


import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerAddressUpdate;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerContactUpdate;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerNameUpdate;
import com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity.CustomerUpdate;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.classify.Classifier;


public class CustomerUpdateClassifier implements Classifier<CustomerUpdate, ItemWriter<? super CustomerUpdate>> {

    private final JdbcBatchItemWriter<CustomerUpdate> recordType1ItemWriter;
    private final JdbcBatchItemWriter<CustomerUpdate> recordType2ItemWriter;
    private final JdbcBatchItemWriter<CustomerUpdate> recordType3ItemWriter;

    public CustomerUpdateClassifier(
            JdbcBatchItemWriter<CustomerUpdate> recordType1ItemWriter,
            JdbcBatchItemWriter<CustomerUpdate> recordType2ItemWriter,
            JdbcBatchItemWriter<CustomerUpdate> recordType3ItemWriter) {

        this.recordType1ItemWriter = recordType1ItemWriter;
        this.recordType2ItemWriter = recordType2ItemWriter;
        this.recordType3ItemWriter = recordType3ItemWriter;
    }

    @Override
    public ItemWriter<CustomerUpdate> classify(CustomerUpdate classifiable) {

        if (classifiable instanceof CustomerNameUpdate) {
            return recordType1ItemWriter;
        } else if (classifiable instanceof CustomerAddressUpdate) {
            return recordType2ItemWriter;
        } else if (classifiable instanceof CustomerContactUpdate) {
            return recordType3ItemWriter;
        } else {
            throw new IllegalArgumentException("Invalid type: " + classifiable.getClass().getCanonicalName());
        }
    }

}
