package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

@CompileStatic
class ManualLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    static <P extends Project> ManualLink<P> manual(JobRef<P> to) {
        return new ManualLink<P>(to, [:], null, ON_SAME_NODE_DISABLED)
    }

    private ManualLink(JobRef<P> to, Map<String, String> predefinedProperties, String propertiesFileName, boolean onSameNode) {
        super(to, predefinedProperties, propertiesFileName, onSameNode)
    }

    @Override
    protected AbstractPublishersFocusedJobChainLink<P> createLink(JobRef<P> to, Map<String, String> predefinedProperties, String propertiesFileName,
                                                                  boolean onSameNode) {
        return new ManualLink<>(to, predefinedProperties, propertiesFileName, onSameNode)
    }

    @Override
    Closure publisherClosureFor(String linkEndJobName) {
        return {
            (delegate as PublisherContext).with {
                buildPipelineTrigger(linkEndJobName) {
                    parameters triggerParameters()
                }
            }
        }
    }
}
