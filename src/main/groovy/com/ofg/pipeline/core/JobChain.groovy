package com.ofg.pipeline.core

import com.ofg.pipeline.core.link.AutoLink
import com.ofg.pipeline.core.link.JobChainLink
import javaposse.jobdsl.dsl.Job

import java.util.function.Consumer

class JobChain<P extends Project> {

    static <P extends Project> JobChain<P> of(JobRef<P> startJob) {
        new JobChain(startJob)
    }

    private final JobRef<P> start
    private final List<JobChainLink<P>> links = []

    private JobChain(JobRef<P> start) {
        this.start = start
    }

    JobChain<P> then(Optional<? extends JobRef<P>> optionalJob) {
        optionalJob.ifPresent({
            return then(optionalJob.get())
        } as Consumer<? super JobRef<P>>)
        return this
    }

    JobChain<P> then(JobRef<P> job) {
        return then(AutoLink.auto(job))
    }

    JobChain<P> then(List<? extends JobRef<P>> jobs) {
        jobs.each { then(it) }
        return this
    }

    JobChain<P> then(JobChainLink link) {
        links.add(link)
        return this
    }

    void linkJobs(Map<JobType, ? extends Job> jobsByType, P project) {
        Job linkStartJob = getJob(jobsByType, start.jobType)
        links.each { JobChainLink<P> link ->
            link.configure(linkStartJob, project)
            linkStartJob = getJob(jobsByType, link.end.jobType)
        }
    }

    private Job getJob(Map<JobType, ? extends Job> jobsByType, JobType jobType) {
        Job linkStartJob = jobsByType[jobType]
        assert linkStartJob, "Attempted to configure links starting from job of type [$jobType], but no such job is defined"
        linkStartJob
    }

}
