package com.ofg.job.component.notifications

import com.ofg.job.component.JobComponent
import com.ofg.pipeline.core.JenkinsVariables
import com.ofg.pipeline.core.Variable
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailContext
import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailSendToContext
import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailTriggersContext

import static com.ofg.job.component.notifications.EmailNotificationTrigger.*
import static com.ofg.job.component.notifications.RecipientType.*

/**
 * @author Szymon Homa
 * @author Artur Gajowy
 * @author Marcin Zajączkowski
 */
class EmailNotifications implements JobComponent<Job> {
    
    private final EmailNotificationsConfiguration configuration
    private final Variable globalToggle
    private final Iterable<EmailNotificationTrigger> triggers
    private Iterable<RecipientType> recipientTypes
    private Closure customEmailConfiguration
    
    private EmailNotifications(
            Variable globalToggle,
            EmailNotificationsConfiguration configuration,
            Iterable<EmailNotificationTrigger> triggers = [FAILURE, UNSTABLE, FIXED],
            Iterable<RecipientType> recipientTypes = [CULPRITS, REQUESTER, ADDITIONAL_RECIPIENTS],
            Closure customEmailConfiguration = {}) {
        this.globalToggle = globalToggle
        this.configuration = configuration
        this.triggers = triggers
        this.recipientTypes = recipientTypes
        this.customEmailConfiguration = customEmailConfiguration
    }
    
    static EmailNotifications configureNotifications(Variable globalToggle, EmailNotificationsConfiguration cfg) {
        return new EmailNotifications(globalToggle, cfg)
    }
    
    static EmailNotifications configureNotifications(EmailNotificationsConfiguration cfg) {
        return new EmailNotifications(null, cfg)
    }
    
    EmailNotifications notifyOn(EmailNotificationTrigger... triggers) {
        return notifyOn(triggers as List)
    }
    
    EmailNotifications notifyOn(Iterable<EmailNotificationTrigger> triggers) {
        return new EmailNotifications(
                this.globalToggle,
                this.configuration,
                triggers,
                this.recipientTypes,
                this.customEmailConfiguration
        )
    }
    
    EmailNotifications sendToRecipients(Iterable<RecipientType> types) {
        return new EmailNotifications(
                this.globalToggle,
                this.configuration,
                this.triggers,
                types,
                this.customEmailConfiguration
        )
    }
    
    EmailNotifications sendToRecipients(RecipientType... types) {
        return sendToRecipients(types as List)
    }
    
    EmailNotifications customConfiguration(@DelegatesTo(ExtendedEmailContext) Closure customEmailConfiguration) {
        return new EmailNotifications(
                this.globalToggle,
                this.configuration,
                this.triggers,
                this.recipientTypes,
                customEmailConfiguration
        );
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
                            delegate.with(customEmailConfiguration)
                            triggers {
                                
                                triggers.each { EmailNotificationTrigger trigger ->
                                    trigger.buildTrigger(owner.delegate as ExtendedEmailTriggersContext, {
                                        sendTo {
                                            content
                                            recipientTypes.each { type -> type.append(owner.delegate as ExtendedEmailSendToContext) }
                                        }
                                    })
                                }
                            }
                        }
                    }
                }
                
            }
        }
    }
    
}
