package com.aks.springbatch.domain.sample.temp.spring_batch_definitive_guide.chapter_10.entity;

import org.springframework.util.StringUtils;

public class CustomerContactUpdate extends CustomerUpdate {
    private final String emailAddress;
    private final String homePhone;
    private final String cellphone;
    private final String workPhone;
    private final Integer notificationPreferences;

    public CustomerContactUpdate(long customerId, String emailAddress, String homePhone, String cellphone, String workPhone, Integer notificationPreferences) {
        super(customerId);
        this.emailAddress = StringUtils.hasText(emailAddress) ? emailAddress : null;
        this.homePhone = StringUtils.hasText(homePhone) ? homePhone : null;
        this.cellphone = StringUtils.hasText(cellphone) ? cellphone : null;
        this.workPhone = StringUtils.hasText(workPhone) ? workPhone : null;
        this.notificationPreferences = notificationPreferences;
    }
}
