package com.ofg.pipeline.core

import javaposse.jobdsl.dsl.Job

interface StageNameConfigurer {
    public void configure(Job job, String stageName, String jobLabel)
}
