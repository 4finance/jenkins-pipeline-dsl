package com.ofg.job.component.reports

import com.ofg.job.component.JobComponent
import javaposse.jobdsl.dsl.Job

class JUnitReport implements JobComponent<Job> {

    private static final String DEFAULT_GRADLE_JUNIT_XML_REPORTS_PATH = '**/build/test-results/**/*.xml'
    
    private final String reportPath
    private final boolean allowEmpty

    private JUnitReport(String reportPath, boolean allowEmpty = false) {
        this.reportPath = reportPath
        this.allowEmpty = allowEmpty
    }

    static JUnitReport publishReportWithGradlePath() {
        return publishReportWithPath(DEFAULT_GRADLE_JUNIT_XML_REPORTS_PATH)
    }

    static JUnitReport publishReportWithPath(String xmlReportsPath) {
        return new JUnitReport(xmlReportsPath)
    }

    JUnitReport allowEmpty(boolean allowEmpty = true) {
        return new JUnitReport(reportPath, allowEmpty)
    }

    @Override
    void applyOn(Job job) {
        job.publishers {
            archiveJunit(reportPath) {
                if (allowEmpty) {
                    allowEmptyResults()
                }
            }
        }
    }
}
