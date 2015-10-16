package com.ofg.pipeline.core

import javaposse.jobdsl.dsl.Job

class StandaloneJobsPipelineTemplate<P extends Project> implements PipelineTemplate<P> {

    private final JobDefinition<? extends Job, P>[] jobDefinitions

    def StandaloneJobsPipelineTemplate(JobDefinition<? extends Job, P>... jobDefinition) {
        this.jobDefinitions = jobDefinition
    }

    @Override
    void configurePipeline(PipelineBuilder<P> pipelineBuilder, P project) {
        pipelineBuilder.configure {
            jobDefinitions.each {
                standaloneJob(it)
            }
        }
    }
}
