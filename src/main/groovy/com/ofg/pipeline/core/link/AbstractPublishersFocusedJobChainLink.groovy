package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import javaposse.jobdsl.dsl.Job

import static com.google.common.base.Preconditions.checkNotNull

abstract class AbstractPublishersFocusedJobChainLink<P extends Project> implements JobChainLink<P> {

    private final JobRef<P> to

    protected AbstractPublishersFocusedJobChainLink(JobRef<P> to) {
        this.to = checkNotNull(to)
    }

    @Override
    JobRef getEnd() {
        return to
    }

    @Override
    void configure(Job linkStartJob, P project) {
        def linkEndJobName = to.getJobName(project)
        Closure configurer = publisherClosureFor(linkEndJobName)
        linkStartJob.with {
            publishers configurer
        }
    }

    abstract protected Closure publisherClosureFor(String linkEndJobName)
}
