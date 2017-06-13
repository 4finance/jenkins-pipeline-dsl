package com.ofg.job.component.notifications

import javaposse.jobdsl.dsl.helpers.publisher.ExtendedEmailSendToContext;

/**
 * @author Szymon Homa
 */
enum RecipientType {
    
    CULPRITS {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.culprits()
        }
    },
    
    REQUESTER {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.requester()
        }
    },
    
    ADDITIONAL_RECIPIENTS {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.recipientList()
        }
    },
    
    DEVELOPERS {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.developers()
        }
    },
    
    FAILING_BUILD_SUSPECTS {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.failingTestSuspects()
        }
    },
    
    FIRST_FAILING_BUILD_SUSPECTS {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.firstFailingBuildSuspects()
        }
    },
    
    UPSTREAM_COMMITTER {
        @Override
        void append(ExtendedEmailSendToContext context) {
            context.upstreamCommitter();
        }
    }
    
    abstract void append(ExtendedEmailSendToContext context)
}
