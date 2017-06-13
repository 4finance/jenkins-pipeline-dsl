package com.ofg.job.component.general

import com.ofg.job.component.JobComponent
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

/**
 * @author Marcin Zajączkowski
 */
@CompileStatic
class TimestampLogger implements JobComponent<Job> {

    @Override
    void applyOn(Job job) {
        job.wrappers {
            timestamps()
        }
    }

    static TimestampLogger logTimestamps() {
        return new TimestampLogger()
    }

}
