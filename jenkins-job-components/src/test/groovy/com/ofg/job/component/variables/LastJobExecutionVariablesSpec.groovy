package com.ofg.job.component.variables

import spock.lang.Specification

class LastJobExecutionVariablesSpec extends Specification {

    def "should be able to enhance and filter keys"() {
        given:
            Map<String, String> originalMap = [
                    'first' : 'firstValue',
                    'second': 'secondValue',
                    'third' : 'thirdValue'
            ]
            Collection<String> wantedParams = [
                    'first', 'second'
            ]
        and:
            String importingScript = LastJobExecutionVariables
                    .injectVariablesFromLastExecutionOfJob("a_job", wantedParams)
                    .importChosenParametersOnly()

        when:
            Map<String, String> transformedMap = Eval.me('lastVariables', originalMap, importingScript) as Map

        then:
            transformedMap.injectedFirst == originalMap.first
            transformedMap.injectedSecond == originalMap.second
            transformedMap.injectedThird == null
    }
    
    def "should be able to enhance keys by non-standard template"() {
        given:
            Map<String, String> originalMap = [
                    'first' : 'firstValue',
            ]
            Collection<String> wantedParams = [
                    'first'
            ]
        and:
            String importingScript = LastJobExecutionVariables
                    .injectVariablesFromLastExecutionOfJob("a_job", wantedParams)
                    .withKeysDecorator('"injected_$key"')
                    .importChosenParametersOnly()

        when:
            Map<String, String> transformedMap = Eval.me('lastVariables', originalMap, importingScript) as Map

        then:
            transformedMap.injected_first == originalMap.first
            transformedMap.injectedFirst == null
    }
}
