package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import javaposse.jobdsl.dsl.Job

import static com.google.common.base.Preconditions.checkNotNull

abstract class AbstractPublishersFocusedJobChainLink<P extends Project> implements JobChainLink<P> {

    protected final static boolean ON_SAME_NODE_DISABLED = false

    private final List<JobRef<P>> to
    protected final boolean onSameNode

    protected AbstractPublishersFocusedJobChainLink(JobRef<P> to, boolean onSameNode) {
        this([checkNotNull(to)], onSameNode)
    }

    protected AbstractPublishersFocusedJobChainLink(List<JobRef<P>> to, boolean onSameNode) {
        checkNotNull(to) && to.collect( {checkNotNull(it, 'job can not be null')})
        this.to = to
        this.onSameNode = onSameNode
    }

    @Override
    JobRef getEnd() {
        return to.first()
    }

    @Override
    void configure(Job linkStartJob, P project) {
        def linkEndJobsName = to.collect( {it.getJobName(project)}).join(",")
        Closure configurer = publisherClosureFor(linkEndJobsName)
        linkStartJob.with {
            publishers configurer
        }
    }

    abstract protected Closure publisherClosureFor(String linkEndJobsName)
}
