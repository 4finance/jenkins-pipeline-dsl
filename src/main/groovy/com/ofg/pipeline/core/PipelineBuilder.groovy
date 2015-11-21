package com.ofg.pipeline.core

import com.google.common.base.Optional
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.google.common.base.Optional.fromNullable

class PipelineBuilder<P extends Project> {

    private final Map<String, List<JobRef<P>>> stages = [:]
    private final List<JobChain<P>> chains = []
    private final JobBuilder<P> jobBuilder
    private final StageNameConfigurer stageNameConfigurer

    PipelineBuilder(DslFactory dslFactory, JobConfigurer<P> jobConfigurer, StageNameConfigurer stageNameConfigurer) {
        this.jobBuilder = new JobBuilder<>(dslFactory, jobConfigurer)
        this.stageNameConfigurer = stageNameConfigurer
    }

    PipelineBuilder configure(@DelegatesTo(PipelineBuilder) Closure closure) {
        this.with(closure)
        return this
    }

    void stage(String stageName, @DelegatesTo(StageContext) closure) {
        assert !stages.containsKey(stageName), "Attempted to define a stage twice. Stage name: [$stageName]"
        stages[stageName] = []
        def stageContext = new StageContext(stageName: stageName)
        stageContext.with(closure)
    }

    private class StageContext {
        private String stageName

        //TODO use the P type instead of ? extends Project in a way that does not trigger type warnings in IDEA
        //(probably impossible without Groovy 2.4)
        //TODO in the meantime, maybe inspect he generic type in runtime and fail fast?
        void job(JobDefinition<? extends Job, ? extends Project> jobDefinition) {
            List<JobType> jobTypesForThisStage = stages[stageName]*.jobType
            assert !jobTypesForThisStage.contains(jobDefinition.jobType),
                    "Attempted to define the same job twice in a single stage." +
                    " Stage: [$stageName], type: [${jobDefinition.jobType}], class: [${jobDefinition.class}]."
            jobBuilder.job(jobDefinition)
            stages[stageName].add(jobDefinition)
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
            def stageName = findStageName(jobDefinition)
            if (stageName.isPresent()) {
                stageNameConfigurer.configure(dslJob, stageName.get(), jobDefinition.jobLabel)
            }
            [jobType, dslJob]
        }
        chains.each {
            it.linkJobs(dslJobs, project)
        }
        return dslJobs.values().toList()
    }

    private Optional<String> findStageName(JobDefinition jobDefinition) {
        def entry = stages.find {
            def stageJobTypes = it.value*.jobType
            stageJobTypes.contains(jobDefinition.jobType)
        }
        return fromNullable(entry?.key)
    }
}

