package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import com.ofg.pipeline.core.TriggerCondition
import com.ofg.pipeline.core.Variable
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

class AutoLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    private final TriggerCondition triggerCondition
    private final Map<String, String> predefinedProperties
    private final String propertiesFileName

    static <P extends Project> AutoLink<P> auto(
            JobRef<P> to,
            TriggerCondition triggerCondition = TriggerCondition.SUCCESS
    ) {
        new AutoLink<P>(to, triggerCondition, [:], null, ON_SAME_NODE_DISABLED)
    }

    private AutoLink(JobRef<P> to, TriggerCondition triggerCondition, Map<String, String> predefinedProperties, String propertiesFileName,
                     boolean onSameNode) {
        super(to, onSameNode)
        this.triggerCondition = triggerCondition
        this.predefinedProperties = predefinedProperties
        this.propertiesFileName = propertiesFileName
    }

    AutoLink<P> withPredefinedProperties(Variable... predefinedProperties) {
        Map<String, String> propertiesMap = predefinedProperties.collectEntries { [it.name(), it.reference] }
        return withPredefinedProperties(propertiesMap)
    }

    AutoLink<P> withPredefinedProperties(Map<String, String> predefinedProperties) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileName, onSameNode)
    }

    AutoLink<P> withPropertiesFile(String propertiesFileName) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileName, onSameNode)
    }

    AutoLink<P> onSameNode(boolean onSameNode = true) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileName, onSameNode)
    }

    @Override
    Closure publisherClosureFor(String linkEndJobsName) {
        return {
            (delegate as PublisherContext).with {
                downstreamParameterized {
                    trigger(linkEndJobsName) {
                        condition(triggerCondition.name())
                        triggerWithNoParameters()
                        parameters {
                            currentBuild()
                            if (onSameNode) {
                                sameNode()
                            }
                            if (predefinedProperties) {
                                predefinedProps(predefinedProperties)
                            }
                            if (propertiesFileName) {
                                propertiesFile(propertiesFileName)
                            }
                        }
                    }
                }
            }
        }
    }
}
