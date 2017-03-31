package com.ofg.job.component.general

import com.ofg.job.component.JobComponent
import javaposse.jobdsl.dsl.Job

class DefaultJdk implements JobComponent<Job> {

    private static final String LATEST_JDK_8 = 'latest_java8'
    
    private final String defaultJdkName;

    private DefaultJdk(String defaultJdkName) {
        this.defaultJdkName = defaultJdkName
    }
    
    /*
    * expects jdk name to be equal to latest_java8
    * */
    static DefaultJdk useLatestJava8() {
        return useJdk(LATEST_JDK_8)
    }
    
    static DefaultJdk useJdk(String jdkName) {
        return new DefaultJdk(jdkName)
    }

    @Override
    void applyOn(Job job) {
        job.jdk(defaultJdkName)
    }
}
