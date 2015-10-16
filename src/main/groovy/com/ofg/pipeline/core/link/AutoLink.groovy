package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import com.ofg.pipeline.core.TriggerCondition
import com.ofg.pipeline.core.Variable
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

public class AutoLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    private final TriggerCondition triggerCondition
    private final Map<String, String> predefinedProperties
    private final List<String> propertiesFileNames

    static <P extends Project> AutoLink<P> auto(
            JobRef<P> to,
            TriggerCondition triggerCondition = TriggerCondition.SUCCESS
    ) {
        new AutoLink<P>(to, triggerCondition, [:], [])
    }

    private AutoLink(JobRef<P> to, TriggerCondition triggerCondition, Map<String, String> predefinedProperties, List<String> propertiesFileNames) {
        super(to)
        this.triggerCondition = triggerCondition
        this.predefinedProperties = predefinedProperties
        this.propertiesFileNames = propertiesFileNames
    }

    AutoLink<P> withPredefinedProperties(Variable... predefinedProperties) {
        Map<String, String> propertiesMap = predefinedProperties.collectEntries { [it.name(), it.reference] }
        return withPredefinedProperties(propertiesMap)
    }

    AutoLink<P> withPredefinedProperties(Map<String, String> predefinedProperties) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileNames)
    }

    AutoLink<P> withPropertiesFiles(String... propertiesFileNames) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileNames.toList())
    }

    @Override
    Closure publisherClosureFor(String linkEndJobName) {
        return {
            (delegate as PublisherContext).with {
                downstreamParameterized {
                    trigger(linkEndJobName, triggerCondition.name(), true) {
                        if (predefinedProperties) {
                            predefinedProps(predefinedProperties)
                        }
                        propertiesFileNames.each {
                            propertiesFile(it)
                        }
                        currentBuild()
                        sameNode()
                    }
                }
            }
        }
    }
}


