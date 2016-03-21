package com.ofg.pipeline.core.link

import com.ofg.pipeline.core.JobRef
import com.ofg.pipeline.core.Project
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

public class ManualLink<P extends Project> extends AbstractPublishersFocusedJobChainLink<P> {

    static <P extends Project> ManualLink<P> manual(JobRef<P> to) {
        new ManualLink<P>(to)
    }

    private ManualLink(JobRef<P> to) {
        super(to)
    }

    @Override
    Closure publisherClosureFor(String linkEndJobName) {
        return {
            (delegate as PublisherContext).with {
                buildPipelineTrigger(linkEndJobName) {
                    parameters {
                        currentBuild()
                    }
                }
            }
        }
    }

}
