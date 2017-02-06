package com.ofg.pipeline.core

import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.DslFactory

import static com.google.common.base.Preconditions.checkNotNull

@CompileStatic
class PipelineTemplateBuilder {

    private final DslFactory dslFactory
    private final JenkinsVariables jenkinsVariables
    private final Map<String, Project> alreadyBuiltProjects = [:]

    PipelineTemplateBuilder(DslFactory dslFactory, JenkinsVariables jenkinsVariables = JenkinsVariables.from([:])) {
        this.dslFactory = checkNotNull(dslFactory)
        this.jenkinsVariables = checkNotNull(jenkinsVariables)
    }

    public <P extends Project> void buildAll(PipelineTemplate<P> pipelineTemplate, Iterable<P> projects) {
        projects.each {
            build(pipelineTemplate, it)
        }
    }

    public <P extends Project> void build(PipelineTemplate<P> pipelineTemplate, P project) {
        ensureNoDuplicateProjects(project)
        //TODO make the configurers specified within pipelineBuilder.configure?
        def jobConfigurer = pipelineTemplate.createJobConfigurer()
        def stageNameConfigurer = pipelineTemplate.createStageNameConfigurer()
        def pipelineBuilder = new PipelineBuilder<P>(dslFactory, jobConfigurer, stageNameConfigurer)
        pipelineTemplate.configurePipeline(pipelineBuilder, project)
        pipelineBuilder.buildJobs(project, jenkinsVariables)
    }

    private void ensureNoDuplicateProjects(Project project) {
        def qualifiedName = project.qualifiedName
        if (alreadyBuiltProjects.containsKey(qualifiedName)) {
            def alredyBuiltProject = alreadyBuiltProjects[qualifiedName]
            throw new UnsupportedOperationException(
                    "Duplicate qualified project name found. Thie conflict is for name '${qualifiedName}', between projects:\n" +
                            " - ${alredyBuiltProject.toString()},\n" +
                            " - ${project.toString()}.\n" +
                            "This could lead to jobs being overwritten. " +
                            "Please make sure all projects built on a single jenkins instance have distinct qualified names."
            )
        }
        alreadyBuiltProjects[qualifiedName] = project
    }

}
