package com.ofg.pipeline.core

//TODO include view creation before opensourcing
interface PipelineTemplate<P extends Project> {
    JobConfigurer<P> createJobConfigurer()
    StageNameConfigurer createStageNameConfigurer()
    void configurePipeline(PipelineBuilder<P> pipelineBuilder, P project)
}