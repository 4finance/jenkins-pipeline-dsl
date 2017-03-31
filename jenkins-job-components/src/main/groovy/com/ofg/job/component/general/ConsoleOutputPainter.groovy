package com.ofg.job.component.general

import com.ofg.job.component.JobComponent
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

/*
* Adds colorful output through ANSIcolor plugin to console
* */
@CompileStatic
class ConsoleOutputPainter implements JobComponent<Job> {

    @Override
    void applyOn(Job job) {
        job.wrappers {
            colorizeOutput()
        }
    }

    static ConsoleOutputPainter outputFullOfColors() {
        return new ConsoleOutputPainter()
    }
}
