package com.ofg.job.component.notifications

import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailTriggerContext

enum EmailNotificationTrigger {

    ABORTED{
        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                aborted(cfg)
            }
        }
    },

    ALWAYS{
        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                always(cfg)
            }
        }
    },

    BEFORE_BUILD{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                beforebuild(cfg)
            }
        }
    },

    FIRST_FAILURE{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                firstfailure(cfg)
            }
        }
    },

    SECOND_FAILURE{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                secondfailure(cfg)
            }
        }
    },

    FAILURE{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                failure(cfg)
            }
        }
    },

    STILL_FAILING{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                stillfailing(cfg)
            }
        }
    },

    FIXED{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                fixed(cfg)
            }
        }
    },

    NOT_BUILT{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                notbuilt(cfg)
            }
        }
    },


    STATUS_CHANGED{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                statuschanged(cfg)
            }
        }
    },


    SUCCESS{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                success(cfg)
            }
        }
    },

    IMPROVEMENT{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                improvement(cfg)
            }
        }
    },

    REGRESSION{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                regression(cfg)
            }
        }
    },

    UNSTABLE{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                unstable(cfg)
            }
        }
    },

    FIRST_UNSTABLE{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                firstunstable(cfg)
            }
        }
    },

    STILL_UNSTABLE{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                stillunstable(cfg)
            }
        }
    },

    FIXED_UNHEALTHY{

        @Override
        Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            return {
                fixedunhealthy(cfg)
            }
        }
    };

    abstract Closure buildTrigger(@DelegatesTo(ExtendedEmailTriggerContext) Closure cfg);
}
