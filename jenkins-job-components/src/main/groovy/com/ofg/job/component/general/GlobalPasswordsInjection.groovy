package com.ofg.job.component.general

import com.ofg.job.component.JobComponent
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

/**
 * @author Marek Kapowicki
 */
@CompileStatic
class GlobalPasswordsInjection implements JobComponent<Job> {

    @Override
    void applyOn(Job job) {
        job.wrappers {
            injectPasswords {
                injectGlobalPasswords()
            }
        }
    }

    static GlobalPasswordsInjection injectGlobalPasswords() {
        return new GlobalPasswordsInjection()
    }

}
