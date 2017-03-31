package com.ofg.job.component.notifications

import com.ofg.job.component.JobComponent
import com.ofg.pipeline.core.JenkinsVariables
import com.ofg.pipeline.core.Variable
import javaposse.jobdsl.dsl.Job

import static com.ofg.job.component.notifications.EmailNotificationTrigger.FAILURE
import static com.ofg.job.component.notifications.EmailNotificationTrigger.FIXED
import static com.ofg.job.component.notifications.EmailNotificationTrigger.UNSTABLE

class EmailNotifications implements JobComponent<Job> {

    private final EmailNotificationsConfiguration configuration
    private final Variable globalToggle
    private final Iterable<EmailNotificationTrigger> triggers

    private EmailNotifications(
            Variable globalToggle,
            EmailNotificationsConfiguration configuration,
            Iterable<EmailNotificationTrigger> triggers = [FAILURE, UNSTABLE, FIXED]) {
        this.globalToggle = globalToggle
        this.configuration = configuration
        this.triggers = triggers
    }

    static EmailNotifications configureNotifications(Variable globalToggle, EmailNotificationsConfiguration cfg) {
        return new EmailNotifications(globalToggle, cfg)
    }

    static EmailNotifications configureNotifications(EmailNotificationsConfiguration cfg) {
        return new EmailNotifications(null, cfg)
    }

    EmailNotifications notifyOn(EmailNotificationTrigger... triggers) {
        return notifyOn(Arrays.asList(triggers))
    }
    
    EmailNotifications notifyOn(Iterable<EmailNotificationTrigger> triggers) {
        return new EmailNotifications(
                globalToggle,
                configuration,
                triggers
        )
    }

    @Override
    void applyOn(Job job) {
        if (notificationsAreAllowed()) {
            addNotificationPublishers(job)
        }
    }

    private boolean notificationsAreAllowed() {
        configuration.areNotificationsAllowed()
    }

    private addNotificationPublishers(Job job) {
        job.publishers {
            flexiblePublish {
                conditionalAction {
                    if (globalToggle) {
                        condition {
                            not { expression('(true)', JenkinsVariables.envReference(globalToggle)) }
                        }
                    }

                    publishers {
                        String additionalRecipients = configuration.additionalRecipients().join(', ')
                        extendedEmail {
                            if (additionalRecipients) {
                                recipientList additionalRecipients
                            }
                            triggers {
                                triggers.each { EmailNotificationTrigger trigger ->
                                    trigger.buildTrigger({
                                        sendTo {
                                            culprits()
                                            requester()
                                            recipientList()
                                        }
                                    }).call()
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}
