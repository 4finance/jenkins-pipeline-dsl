package com.ofg.pipeline.core

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import java.util.function.Consumer

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

    //Cannot be private as an external closure is called in its context
    class StageContext {
        private String stageName

        void job(Optional<JobDefinition<? extends Job, P>> optionalJobDefinition) {
            optionalJobDefinition.ifPresent({
                job(optionalJobDefinition.get())
            } as Consumer<? super JobDefinition<? extends Job, P>>)
        }

        void job(JobDefinition<? extends Job, P> jobDefinition) {
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

