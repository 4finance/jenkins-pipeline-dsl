package com.ofg.job.component.notifications

import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailTriggerContext
import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailTriggersContext

enum EmailNotificationTrigger {
    
    ABORTED {
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.aborted(cfg)
        }
    },
    
    ALWAYS {
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.always(cfg)
        }
    },
    
    BEFORE_BUILD {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.beforebuild(cfg)
        }
    },
    
    FIRST_FAILURE {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.firstfailure(cfg)
        }
    },
    
    SECOND_FAILURE {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.secondfailure(cfg)
        }
    },
    
    FAILURE {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.failure(cfg)
        }
    },
    
    STILL_FAILING {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.stillfailing(cfg)
        }
    },
    
    FIXED {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.fixed(cfg)
        }
    },
    
    NOT_BUILT {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.notbuilt(cfg)
        }
    },
    
    STATUS_CHANGED {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.statuschanged(cfg)
        }
    },
    
    SUCCESS {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.success(cfg)
        }
    },
    
    IMPROVEMENT {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.improvement(cfg)
        }
    },
    
    REGRESSION {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.regression(cfg)
        }
    },
    
    UNSTABLE {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.unstable(cfg)
        }
    },
    
    FIRST_UNSTABLE {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.firstUnstable(cfg)
        }
    },
    
    STILL_UNSTABLE {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.stillUnstable(cfg)
        }
    },
    
    FIXED_UNHEALTHY {
        
        @Override
        void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg) {
            context.fixedUnhealthy(cfg)
        }
    };
    
    abstract void buildTrigger(ExtendedEmailTriggersContext context, @DelegatesTo(ExtendedEmailTriggerContext) Closure cfg);
}
