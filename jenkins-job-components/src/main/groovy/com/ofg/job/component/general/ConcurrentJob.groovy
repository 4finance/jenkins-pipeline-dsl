package com.ofg.job.component.general

import com.ofg.job.component.JobComponent
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

/**
 * Enables concurrent job execution
 * @author Marek Kapowicki
 */
@CompileStatic
class ConcurrentJob implements JobComponent<Job> {

    static ConcurrentJob enableConcurrentExecution() {
        return new ConcurrentJob();
    }

    @Override
    void applyOn(Job job) {
        job.concurrentBuild()
    }
}
