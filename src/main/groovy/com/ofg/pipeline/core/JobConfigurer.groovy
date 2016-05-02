package com.ofg.pipeline.core

import javaposse.jobdsl.dsl.Job

interface JobConfigurer<P extends Project> {
    void preConfigure(Job job, P project, JenkinsVariables jenkinsVariables)
    void postConfigure(Job job, P project, JenkinsVariables jenkinsVariables)
}