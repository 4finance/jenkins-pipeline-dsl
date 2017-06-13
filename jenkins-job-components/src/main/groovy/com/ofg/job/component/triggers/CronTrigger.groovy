package com.ofg.job.component.triggers

import com.ofg.job.component.JobComponent
import javaposse.jobdsl.dsl.Job


/**
 * Triggers job on a given cron
 *
 * @see CronBuilder
 * @author Szymon Homa
 */
class CronTrigger implements JobComponent<Job> {
    
    public static final CronTrigger NEVER = new CronTrigger(null)
    
    private final String cronDef;
    
    private CronTrigger(String cronDef) {
        this.cronDef = cronDef
    }
    
    static CronTrigger triggerOn(CronBuilder builder) {
        return new CronTrigger(builder.build())
    }
    
    static CronTrigger triggerOn(String cronDef) {
        return new CronTrigger(cronDef)
    }
    
    static CronTrigger never() {
        return NEVER
    }
    
    @Override
    void applyOn(Job job) {
        if (cronDef) {
            job.triggers {
                cron cronDef
            }
        }
    }
}
