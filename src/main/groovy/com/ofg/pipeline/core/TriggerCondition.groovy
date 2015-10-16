package com.ofg.pipeline.core

/**
 * Enumerates valid {@code condition}-s that can be passed to
 * {@link javaposse.jobdsl.dsl.helpers.common.DownstreamContext#trigger(java.lang.String, java.lang.String)} method and
 * its variants
 */
enum TriggerCondition {
    SUCCESS, UNSTABLE, UNSTABLE_OR_BETTER, UNSTABLE_OR_WORSE, FAILED, ALWAYS
}
