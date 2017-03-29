package com.ofg.pipeline.core

import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.ConfigFile
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Folder
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.MavenSettingsConfigFile
import javaposse.jobdsl.dsl.jobs.BuildFlowJob
import javaposse.jobdsl.dsl.jobs.FreeStyleJob
import javaposse.jobdsl.dsl.jobs.IvyJob
import javaposse.jobdsl.dsl.jobs.MatrixJob
import javaposse.jobdsl.dsl.jobs.MavenJob
import javaposse.jobdsl.dsl.jobs.MultiJob
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob
import javaposse.jobdsl.dsl.jobs.WorkflowJob

import static com.google.common.base.Preconditions.checkNotNull

@CompileStatic
class JobFactory {
    
    private final DslFactory dslFactory
    
    JobFactory(DslFactory dslFactory) {
        this.dslFactory = checkNotNull(dslFactory)
    }
    
    protected <T extends Job> T create(Class<T> jobClass, String jobName) {
        def job = createJob(jobClass, jobName)
        assert jobClass.isInstance(job), "Couldn't create an instance of $jobClass.simpleName, probably it's unsupported yet"
        return job
    }
    
    private <T extends Job> T createJob(Class<T> jobClass, String jobName) {
        switch (jobClass) {
            case WorkflowJob: return (T) dslFactory.workflowJob(jobName)
            case MultiJob: return (T) dslFactory.multiJob(jobName)
            case MatrixJob: return (T) dslFactory.matrixJob(jobName)
            case BuildFlowJob: return (T) dslFactory.buildFlowJob(jobName)
            case FreeStyleJob: return (T) dslFactory.freeStyleJob(jobName)
            case MavenJob: return (T) dslFactory.mavenJob(jobName)
            case IvyJob: return (T) dslFactory.ivyJob(jobName)
            default: return (T) dslFactory.job(jobName)
        }
    }
}
