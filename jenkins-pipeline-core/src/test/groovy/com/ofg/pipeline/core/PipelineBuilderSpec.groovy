package com.ofg.pipeline.core

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.JobParent
import spock.lang.Issue
import spock.lang.Specification

class PipelineBuilderSpec extends Specification {

    @Issue("https://github.com/4finance/jenkins-pipeline-dsl/issues/21")
    //TODO: It passes even with private PipelineBuilder.StageContext when a test is in jenkins-pipeline-dsl itself
    //      Fails as expected when put in a project that uses jenkins-pipeline-dsl as a dependency...
    def "should not fail with IllegalAccessError with private StageContext"() {
        given:
            DslFactory dslFactory = {} as JobParent
            JobConfigurer<Project> jobConfigurer = {} as JobConfigurer
            StageNameConfigurer stageNameConfigurer = {} as StageNameConfigurer
        and:
            CompileStaticStageBuilder stageBuilder = new CompileStaticStageBuilder(dslFactory, jobConfigurer, stageNameConfigurer)
        when:
            stageBuilder.addStage()
        then:
            noExceptionThrown()
    }
}

@CompileStatic
@TupleConstructor(includeFields = true)
class CompileStaticStageBuilder {

    private PipelineBuilder<Project> pipelineBuilder

    CompileStaticStageBuilder(DslFactory dslFactory, JobConfigurer<Project> jobConfigurer, StageNameConfigurer stageNameConfigurer) {
        pipelineBuilder = new PipelineBuilder<Project>(dslFactory, jobConfigurer, stageNameConfigurer)
    }

    void addStage() {
        pipelineBuilder.stage("testStage", { job(Optional.empty()) })
    }
}
