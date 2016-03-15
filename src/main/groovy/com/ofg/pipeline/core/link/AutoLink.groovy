package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import com.ofg.pipeline.core.TriggerCondition
import com.ofg.pipeline.core.Variable
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

public class AutoLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    private final TriggerCondition triggerCondition
    private final Map<String, String> predefinedProperties
    private final String propertiesFileName

    static <P extends Project> AutoLink<P> auto(
            JobRef<P> to,
            TriggerCondition triggerCondition = TriggerCondition.SUCCESS
    ) {
        new AutoLink<P>(to, triggerCondition, [:], [])
    }

    private AutoLink(JobRef<P> to, TriggerCondition triggerCondition, Map<String, String> predefinedProperties, String propertiesFileName) {
        super(to)
        this.triggerCondition = triggerCondition
        this.predefinedProperties = predefinedProperties
        this.propertiesFileName = propertiesFileName
    }

    AutoLink<P> withPredefinedProperties(Variable... predefinedProperties) {
        Map<String, String> propertiesMap = predefinedProperties.collectEntries { [it.name(), it.reference] }
        return withPredefinedProperties(propertiesMap)
    }

    AutoLink<P> withPredefinedProperties(Map<String, String> predefinedProperties) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, null)
    }

    AutoLink<P> withPropertiesFiles(String... propertiesFileName) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileName)
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
                        if (propertiesFileName) {
                            propertiesFile(propertiesFileName)
                        }
                        currentBuild()
                        sameNode()
                    }
                }
            }
        }
    }
}


