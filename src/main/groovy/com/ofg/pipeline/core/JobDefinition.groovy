package com.ofg.pipeline.core

import com.google.common.base.CaseFormat
import javaposse.jobdsl.dsl.Job

abstract class JobDefinition<T extends Job, P extends Project> implements JobRef<P> {

    private static final JOB_TYPE_FRAGMENT_SEPARATOR = '-'

    abstract Class<T> getJobClass()

    JobType getJobType() {
        return new JobType(camelCaseToJobTypeFormat(this.class.simpleName))
    }

    protected final String camelCaseToJobTypeFormat(String camelCase) {
        def type = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelCase)
        return type.replaceAll('_', JOB_TYPE_FRAGMENT_SEPARATOR)
    }

    String getJobLabel() {
        jobType.name.replaceAll(JOB_TYPE_FRAGMENT_SEPARATOR, ' ').capitalize()
    }

    String getJobName(P project) {
        return "${project.qualifiedName}-${jobType.name}"
    }

    abstract void configure(T job, P project, JenkinsVariables jenkinsVariables)

}



