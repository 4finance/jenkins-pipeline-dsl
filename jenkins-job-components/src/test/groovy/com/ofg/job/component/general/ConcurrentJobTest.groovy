package com.ofg.job.component.general

import com.ofg.pipeline.test.util.JobSpecTrait
import javaposse.jobdsl.dsl.Job
import spock.lang.Specification


class ConcurrentJobTest extends Specification implements JobSpecTrait {
    
    def "should succesfully apply on #job.class.simpleName"() {
        expect:
            ConcurrentJob.enableConcurrentExecution()
                         .applyOn job
        
        where:
            job << [
                    createJobParent().job("job"),
                    createJobParent().ivyJob("job"),
                    createJobParent().mavenJob("job"),
                    createJobParent().buildFlowJob("job"),
                    createJobParent().pipelineJob("job")
            ]
    }
}
