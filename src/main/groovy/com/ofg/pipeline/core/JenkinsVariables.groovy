package com.ofg.pipeline.core


class JenkinsVariables {
    static JenkinsVariables from(Script script) {
        from(script.binding.variables)
    }

    static JenkinsVariables from(Map variables) {
        new JenkinsVariables(variables)
    }

    static String reference(Variable variable) {
        return "\${${variable.name()}}"
    }

    static String envReference(Variable variable) {
        return "\${ENV,var=\"${variable.name()}\"}"
    }

    private final Map<String, String> variables

    private JenkinsVariables(Map variables) {
        this.variables = (variables.collectEntries {k, v -> [k.toString(), v.toString()]} as Map<String, String>)
                .asImmutable()
    }

    String get(Variable variable) {
        def name = variable.name()
        if (!variables.containsKey(name)) {
            throw new NoSuchElementException("Jenkins variable '$name' not found")
        }
        return variables[name]
    }

    String get(Variable variable, String defaultValue) {
        def name = variable.name()
        return variables.containsKey(name) ? variables[name] : defaultValue
    }

    boolean getBoolean(Variable variable, boolean defaultValue) {
        def name = variable.name()
        return variables.containsKey(name) ? Boolean.parseBoolean(variables[name]) : defaultValue
    }
}
