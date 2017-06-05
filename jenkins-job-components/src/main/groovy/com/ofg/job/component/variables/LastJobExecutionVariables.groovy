package com.ofg.job.component.variables

import com.ofg.job.component.JobComponent
import javaposse.jobdsl.dsl.Job

/**
 * @author Szymon Homa
 * @author Marek Kapowicki
 */
class LastJobExecutionVariables implements JobComponent<Job> {

    private final Collection<String> variablesToImport
    private final String jobName
    private final String keyDecorator

    private LastJobExecutionVariables(String jobName, Collection<String> variablesToImport, String keyTemplate) {
        this.jobName = jobName
        this.variablesToImport = variablesToImport
        this.keyDecorator = keyTemplate
    }

    /**
     * 
     * @param jobName
     * @param variablesToImport
     * when created like that, it will decorateAll keys by the code: '"injected${key.substring(0, 1).toUpperCase()}${key.substring(1)}"'
     * which behaves as:
     *          param -> injectedParam
     */
    static LastJobExecutionVariables injectVariablesFromLastExecutionOfJob(String jobName, Collection<String> variablesToImport) {
        return new LastJobExecutionVariables(jobName, variablesToImport, '"injected${key.substring(0, 1).toUpperCase()}${key.substring(1)}"')
    }
    
    /**
    *
    * It expects to get standard String, that will be used as a GString
    * when transforming the imported parameters map.
    * @example '"injected_$key"'
    * available parameters in GString are: String key, String value
    */
    LastJobExecutionVariables withKeysDecorator(String keyDecorator) {
        return new LastJobExecutionVariables(
                this.jobName,
                this.variablesToImport,
                keyDecorator
        )
    }

    @Override
    void applyOn(Job job) {
        job.with {
            wrappers {
                environmentVariables {
                    groovy buildScript()
                }
            }
        }
    }

    private String buildScript() {
        return """
            ${fetchAllVariablesFromJob()}
            ${importChosenParametersOnly()}
        """.stripIndent()
    }

    private String fetchAllVariablesFromJob() {
        return """
            import jenkins.model.Jenkins

            Map<String, String> lastVariables = Jenkins.instance.getItem('${jobName}').lastStableBuild.buildVariables
        """
    }

    private String importChosenParametersOnly() {
        return """
            List<String> argsToImport = [
                ${variablesToImport.collect({ return "'$it'" }).join(',')}
            ]
            return lastVariables
                       .findAll({ String key, String value -> argsToImport.contains(key) })
                       .collectEntries({ String key, String value -> 
                            [(${keyDecorator}.toString()):value]
                       })
        """
    }
}
