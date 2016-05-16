package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import com.ofg.pipeline.core.TriggerCondition
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

class AutoLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    private final TriggerCondition triggerCondition

    static <P extends Project> AutoLink<P> auto(
            JobRef<P> to,
            TriggerCondition triggerCondition = TriggerCondition.SUCCESS) {
        return new AutoLink<P>(to, triggerCondition, [:], null, ON_SAME_NODE_DISABLED)
    }

    private AutoLink(JobRef<P> to, TriggerCondition triggerCondition, Map<String, String> predefinedProperties, String propertiesFileName,
                     boolean onSameNode) {
        super(to, predefinedProperties, propertiesFileName, onSameNode)
        this.triggerCondition = triggerCondition
    }

    @Override
    protected AutoLink<P> createLink(JobRef<P> to, Map<String, String> predefinedProperties, String propertiesFileName, boolean onSameNode) {
        return new AutoLink<P>(end, triggerCondition, predefinedProperties, propertiesFileName, onSameNode)
    }

    @Override
    Closure publisherClosureFor(String linkEndJobName) {
        return {
            (delegate as PublisherContext).with {
                downstreamParameterized {
                    trigger(linkEndJobName) {
                        condition(triggerCondition.name())
                        triggerWithNoParameters()
                        parameters triggerParameters()
                    }
                }
            }
        }
    }
}
