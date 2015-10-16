package com.ofg.pipeline.core

public interface JobRef<P extends Project> {
    JobType getJobType()
    String getJobName(P project)
}
