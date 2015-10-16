package com.ofg.pipeline.core

import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.MemoryJobManagement
import javaposse.jobdsl.dsl.jobs.BuildFlowJob
import javaposse.jobdsl.dsl.jobs.FreeStyleJob
import javaposse.jobdsl.dsl.jobs.MatrixJob
import javaposse.jobdsl.dsl.jobs.MultiJob
import javaposse.jobdsl.dsl.jobs.WorkflowJob
import spock.lang.Specification
import spock.lang.Unroll

class JobFactorySpec extends Specification {

    def dslFactory = createJobParent()

    @Unroll
    def 'should create job of type #jobClass.simpleName'() {
        given:
            def jobFactory = new JobFactory(dslFactory)

        expect:
            jobClass.isInstance(jobFactory.create(jobClass, 'Job name'))

        where:
            jobClass << [Job, FreeStyleJob, BuildFlowJob, MatrixJob, WorkflowJob, MultiJob]
    }

    def 'should throw an exception for unsupported job subtype'() {
        given:
            def jobFactory = new JobFactory(dslFactory)

        when:
            jobFactory.create(UnforeseenJobType, "Ooops!")

        then:
            def exception = thrown(AssertionError)
            exception.message.contains("Couldn't create an instance ")
            exception.message.contains('unsupported yet')
    }

    static class UnforeseenJobType extends Job {
        protected UnforeseenJobType(JobManagement jobManagement) {
            super(jobManagement)
        }
    }

    JobParent createJobParent() {
        JobParent jp = new JobParent() {
            @Override
            Object run() {
                return null
            }
        }
        JobManagement jm = new MemoryJobManagement()
        jp.setJm(jm)
        return jp
    }

}
