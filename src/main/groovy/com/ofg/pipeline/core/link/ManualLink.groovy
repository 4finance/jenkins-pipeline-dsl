package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

class ManualLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    static <P extends Project> ManualLink<P> manual(JobRef<P> to) {
        new ManualLink<P>(to, ON_SAME_NODE_DISABLED)
    }

    private ManualLink(JobRef<P> to, boolean onSameNode) {
        super(to, onSameNode)
    }

    ManualLink<P> onSameNode(boolean onSameNode = true) {
        return new ManualLink<P>(end, onSameNode)
    }

    @Override
    Closure publisherClosureFor(String linkEndJobName) {
        return {
            (delegate as PublisherContext).with {
                buildPipelineTrigger(linkEndJobName) {
                    parameters {
                        currentBuild()
                        if (onSameNode) {
                            sameNode()
                        }
                    }
                }
            }
        }
    }

}
