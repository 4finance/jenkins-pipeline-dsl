package com.ofg.pipeline.core

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class PipelineBuilder<P extends Project> {

    private final JobBuilder<P> jobBuilder
    private final Map<JobRef<? extends Project>, String> stageNamesForJobs = [:]
    private final StageNameConfigurer stageNameConfigurer
    private final List<JobChain<P>> chains = []

    PipelineBuilder(DslFactory dslFactory, JobConfigurer<P> jobConfigurer, StageNameConfigurer stageNameConfigurer) {
        this.jobBuilder = new JobBuilder<>(dslFactory, jobConfigurer)
        this.stageNameConfigurer = stageNameConfigurer
    }

    PipelineBuilder configure(@DelegatesTo(PipelineBuilder) Closure closure) {
        this.with(closure)
        return this
    }

    void stage(String stageName, @DelegatesTo(StageContext) closure) {
        def stageContext = new StageContext(stageName: stageName)
        stageContext.with(closure)
    }

    private class StageContext {
        private String stageName

        //TODO use the P type instead of ? extends Project in a way that does not trigger type warnings in IDEA
        //(probably impossible without Groovy 2.4)
        //TODO in the meantime, maybe inspect he generic type in runtime and fail fast?
        void job(JobDefinition<? extends Job, ? extends Project> jobDefinition) {
            jobBuilder.job(jobDefinition)
            stageNamesForJobs[jobDefinition] = stageName
        }
    }

    void standaloneJob(JobDefinition<? extends Job, P> job) {
        jobBuilder.job(job)
    }

    void chain(JobChain<P> chain) {
        chains.add(chain)
    }

    //TODO make it void again once the pipieline dsl migration is done
    List<? extends Job> buildJobs(P project, JenkinsVariables jenkinsVariables) {
        Map<JobType, Job> dslJobs = jobBuilder.jobs.collectEntries { JobType jobType, JobDefinition jobDefinition ->
            def dslJob = jobBuilder.buildJob(jobDefinition, project, jenkinsVariables)
            def stageName = stageNamesForJobs[jobDefinition]
            if (stageName) {
                stageNameConfigurer.configure(dslJob, stageName, jobDefinition.jobLabel)
            }
            [jobType, dslJob]
        }
        chains.each {
            it.linkJobs(dslJobs, project)
        }
        return dslJobs.values().toList()
    }
}

