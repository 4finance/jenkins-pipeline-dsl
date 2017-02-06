package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import com.ofg.pipeline.core.Variable
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.helpers.common.DownstreamTriggerParameterContext

import static com.google.common.base.Preconditions.checkNotNull

@CompileStatic
abstract class AbstractPublishersFocusedJobChainLink<P extends Project> implements JobChainLink<P> {

    protected final static boolean ON_SAME_NODE_DISABLED = false

    private final JobRef<P> to

    protected final Map<String, String> predefinedProperties
    protected final String propertiesFileName
    protected final boolean onSameNode

    protected AbstractPublishersFocusedJobChainLink(JobRef<P> to, Map<String, String> predefinedProperties, String propertiesFileName,
                                                    boolean onSameNode) {
        this.to = checkNotNull(to)
        this.predefinedProperties = predefinedProperties
        this.propertiesFileName = propertiesFileName
        this.onSameNode = onSameNode
    }

    @Override
    JobRef getEnd() {
        return to
    }

    @Override
    void configure(Job linkStartJob, P project) {
        def linkEndJobName = to.getJobName(project)
        Closure configurer = publisherClosureFor(linkEndJobName)
        linkStartJob.with {
            publishers configurer
        }
    }

    AbstractPublishersFocusedJobChainLink<P> withPredefinedProperties(Variable... predefinedProperties) {
        Map<String, String> propertiesMap = predefinedProperties.collectEntries { [it.name(), it.reference] }
        return withPredefinedProperties(propertiesMap)
    }

    AbstractPublishersFocusedJobChainLink<P> withPredefinedProperties(Map<String, String> predefinedProperties) {
        return createLink(end, predefinedProperties, propertiesFileName, onSameNode)
    }

    AbstractPublishersFocusedJobChainLink<P> withPropertiesFile(String propertiesFileName) {
        return createLink(end, predefinedProperties, propertiesFileName, onSameNode)
    }

    AbstractPublishersFocusedJobChainLink<P> onSameNode(boolean onSameNode = true) {
        return createLink(end, predefinedProperties, propertiesFileName, onSameNode)
    }

    protected Closure triggerParameters() {
        return {
            (delegate as DownstreamTriggerParameterContext).with {
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

    abstract protected AbstractPublishersFocusedJobChainLink<P> createLink(JobRef<P> to, Map<String, String> predefinedProperties,
                                                                           String propertiesFileName, boolean onSameNode);

    abstract protected Closure publisherClosureFor(String linkEndJobName)
}
