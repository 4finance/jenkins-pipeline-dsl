package com.ofg.pipeline.core

/**
 * Represents a project for which the Pipeline DSL is used to build a pipeline for. The only requirement
 * on a {@code Project} is that it has a unique qualified name that can be used to differentiate names of jobs created
 * for different {@code Project}-s. The rest is up to the DSL users.
 * <p>
 * The users are always handed concrete implementations of this interface thanks to the &lt;P extends Project&gt;
 * parametrizations (see {@link JobDefinition} for an example). The interface exists solely to impose the 'namablity'
 * requirement. If not for the requirement, the parametrizations would be just &lt;P&gt;.
 */
interface Project {
    String getQualifiedName()
}
