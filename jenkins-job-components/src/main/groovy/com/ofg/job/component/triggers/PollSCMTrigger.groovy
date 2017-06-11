package com.ofg.job.component.triggers

import com.ofg.job.component.JobComponent
import javaposse.jobdsl.dsl.Job

/**
 * Enables scm polling on intervals defined by a given cron
 *
 * @author Szymon Homa
 * @author Konrad Kamil Dobrzyński
 */
class PollSCMTrigger implements JobComponent<Job> {

    public static final PollSCMTrigger NONE = new PollSCMTrigger(null)
    
    private final String cronDef

    static PollSCMTrigger none() {
        return NONE
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
