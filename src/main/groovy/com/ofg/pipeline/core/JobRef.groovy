package com.ofg.pipeline.core

interface JobRef<P extends Project> {
    JobType getJobType()
    String getJobName(P project)
}
