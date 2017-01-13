package com.ofg.pipeline.core

import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

@CompileStatic
class JobBuilder<P extends Project> {

    private final Map<JobType, JobDefinition> jobs = [:]

    private final JobConfigurer<P> jobConfigurer
    private final DslFactory dslFactory
    private final JobFactory jobFactory

    JobBuilder(DslFactory dslFactory, JobConfigurer<P> jobConfigurer) {
        this.dslFactory = dslFactory
        this.jobConfigurer = jobConfigurer
        this.jobFactory = new JobFactory(dslFactory)
    }

    protected Map<JobType, ? extends JobDefinition> getJobs() {
        return jobs
    }

    void job(Class<? extends JobDefinition> jobDefinitionClass) {
        job(jobDefinitionClass.newInstance())
    }

    void job(JobDefinition jobDefinition) {
        def jobType = jobDefinition.jobType
        assert !jobs.containsKey(jobType),
                "Attempted to define the same job twice. Type: [${jobType}], class: [${jobDefinition.class}]."
        jobs.put(jobType, jobDefinition)
    }

    protected <T extends Job> T buildJob(JobDefinition<T, P> jobDefinition, P project, JenkinsVariables jenkinsVariables) {
        def jobName = jobDefinition.getJobName(project)
        def dslJob = jobFactory.create(jobDefinition.jobClass, jobName)
        jobConfigurer.preConfigure(dslJob, project, jenkinsVariables)
        jobDefinition.configure(dslJob, project, jenkinsVariables)
        jobConfigurer.postConfigure(dslJob, project, jenkinsVariables)
        return dslJob
    }

    void buildJobs(P project, JenkinsVariables jenkinsVariables) {
        jobs.values().each {
            buildJob(it, project, jenkinsVariables)
        }
    }
}
