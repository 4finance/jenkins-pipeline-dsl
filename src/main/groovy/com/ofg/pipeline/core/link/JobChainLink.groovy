package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import javaposse.jobdsl.dsl.Job

interface JobChainLink<P extends Project> {
    JobRef<P> getEnd()
    void configure(Job linkStartJob, P project)
}


