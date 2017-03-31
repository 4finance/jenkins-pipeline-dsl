package com.ofg.job.component.triggers

import com.ofg.job.component.JobComponent
import javaposse.jobdsl.dsl.Job

class PollSCMTrigger implements JobComponent<Job> {

    private final String cronDef

    static PollSCMTrigger none() {
        return new PollSCMTrigger(null)
    }

    static PollSCMTrigger onCron(@DelegatesTo(CronBuilder) Closure cronDefinition) {
        return onCron(
                CronBuilder.defineAs(cronDefinition)
        )
    }

    static PollSCMTrigger onCron(CronBuilder cronBuilder) {
        return onCron(cronBuilder.build())
    }

    static PollSCMTrigger onCron(String cron) {
        return new PollSCMTrigger(cron)
    }

    private PollSCMTrigger(String cronDef) {
        this.cronDef = cronDef
    }

    @Override
    void applyOn(Job jobToTrigger) {
        if (cronDef) {
            jobToTrigger.triggers {
                scm(cronDef)
            }
        }
    }
}
