package com.ofg.pipeline.core

import com.google.common.base.CaseFormat
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

@CompileStatic
abstract class JobDefinition<T extends Job, P extends Project> implements JobRef<P> {

    private static final String JOB_TYPE_FRAGMENT_SEPARATOR = '-'

    abstract Class<T> getJobClass()

    JobType getJobType() {
        return new JobType(toJobTypeFromUpperCamel(this.class.simpleName))
    }

    protected final String toJobTypeFromUpperCamel(String camelCase) {
        return toJobTypeFormatFrom(CaseFormat.UPPER_CAMEL, camelCase)
    }

    protected final String toJobTypeFromUpperUnderscore(String snakeCase) {
        return toJobTypeFormatFrom(CaseFormat.UPPER_UNDERSCORE, snakeCase)
    }

    private String toJobTypeFormatFrom(CaseFormat caseFormat, String snakeCase) {
        def type = caseFormat.to(CaseFormat.LOWER_UNDERSCORE, snakeCase)
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



