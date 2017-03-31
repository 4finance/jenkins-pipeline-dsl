package com.ofg.job.component.general

import com.ofg.job.component.JobComponent
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

@CompileStatic
/*
* Allows adding some additional output for the task, by utilizing system shell.
* right now it uses simple "echo" 
* */
class ConsoleLogger implements JobComponent<Job> {

    private final String command;

    private ConsoleLogger(String toLog) {
        this.command = "echo \"$toLog\""
    }
    
    static ConsoleLogger logInShell(String toLog) {
        return new ConsoleLogger(toLog)
    }

    @Override
    void applyOn(Job job) {
        job.with {
            steps {
                shell(command)
            }
        }
    }
}
