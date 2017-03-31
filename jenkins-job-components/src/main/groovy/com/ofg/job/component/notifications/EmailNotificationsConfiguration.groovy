package com.ofg.job.component.notifications

import groovy.transform.CompileStatic
import groovy.transform.PackageScope

@CompileStatic
class EmailNotificationsConfiguration {

    private final boolean areAllowed
    private final List<String> additionalRecipients = []

    static EmailNotificationsConfiguration notificationsDisabled() {
        return new EmailNotificationsConfiguration(false)
    }

    static EmailNotificationsConfiguration allowNotifications(
            @DelegatesTo(EmailNotificationsConfiguration) Closure setup = Closure.IDENTITY) {
        EmailNotificationsConfiguration cfg = new EmailNotificationsConfiguration(true);
        cfg.with(setup)
        return cfg;
    }

    private EmailNotificationsConfiguration(boolean areAllowed) {
        this.areAllowed = areAllowed
    }

    void withAdditionalRecipients(String... recipients) {
        withAdditionalRecipients(Arrays.asList(recipients))
    }

    void withAdditionalRecipients(Collection<String> recipients) {
        additionalRecipients.addAll(recipients)
    }

    @PackageScope
    boolean areNotificationsAllowed() {
        return areAllowed
    }

    @PackageScope
    List<String> additionalRecipients() {
        return additionalRecipients
    }
}
