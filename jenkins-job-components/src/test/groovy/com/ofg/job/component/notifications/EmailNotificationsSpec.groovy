package com.ofg.job.component.notifications

import com.ofg.pipeline.test.AbstractJobXmlComparingSpec
import com.ofg.pipeline.test.util.JobSpecTrait
import javaposse.jobdsl.dsl.DslFactory

import static com.ofg.job.component.notifications.EmailNotificationsConfiguration.allowNotifications

class EmailNotificationsSpec extends AbstractJobXmlComparingSpec implements JobSpecTrait {
    
    private static final String customJob = 'test_job_with_custom_config';
    private static final String defaultJob = 'test_job_with_defaults'
    
    List<String> expectedJobs = [defaultJob, customJob]
    File expectedJobXmlsPath = new File("./src/test/resources/component/notifications/email")
    
    @Override
    void setupJobs(DslFactory dslFactory) {
        EmailNotifications.configureNotifications(allowNotifications())
                          .applyOn(dslFactory.job(defaultJob))
        
        EmailNotifications.configureNotifications(allowNotifications {
            withAdditionalRecipients('some@email.com', 'other@email.com')
        }).customConfiguration({
            defaultContent = 'some custom content'
            defaultSubject = 'some custom subject'
        }).applyOn(dslFactory.job(customJob))
    }
    
}
