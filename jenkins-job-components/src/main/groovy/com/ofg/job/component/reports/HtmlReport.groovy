package com.ofg.job.component.reports

import com.ofg.job.component.JobComponent
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job

/**
 * @author Tomasz Krzemiński
 * @author Konrad Kamil Dobrzyński
 */
@CompileStatic
class HtmlReport implements JobComponent<Job> {

    private static final String DEFAULT_REPORT_NAME = "HTML Report"
    
    private final String htmlReportPath;
    private final String reportName

    static HtmlReport onPath(String path) {
        return reportInPath(DEFAULT_REPORT_NAME, path)
    }
    
    static HtmlReport reportInPath(String name, String path) {
        return new HtmlReport(name, path)
    }

    private HtmlReport(String name, String path) {
        this.reportName = name
        this.htmlReportPath = path
    }

    @Override
    void applyOn(Job job) {
        if (htmlReportPath) {
            job.publishers {
                publishHtml {
                    report(htmlReportPath) {
                        reportName(reportName)
                        keepAll()
                        alwaysLinkToLastBuild()
                    }
                }
            }
        }
    }
}