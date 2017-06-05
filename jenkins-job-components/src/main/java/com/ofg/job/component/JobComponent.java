package com.ofg.job.component;

import javaposse.jobdsl.dsl.Job;

/**
 * Interface for configurations applicable on Job.
 * @author Szymon Homa
 * @author Marcin Zajączkowski
 */
public interface JobComponent<J extends Job> {

    JobComponent<? extends Job> EMPTY = job -> {};
    
    default <NJ extends J> JobComponent<NJ> and(JobComponent<NJ> other) {
        return (job) -> {
            this.applyOn(job);
            other.applyOn(job);
        };
    }

    void applyOn(J job);
}
