package com.ofg.job.component

import groovy.test.GroovyAssert
import groovy.transform.CompileStatic
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.jobs.MatrixJob
import javaposse.jobdsl.dsl.jobs.MultiJob
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import spock.lang.Specification

class JobComponentSpec extends Specification {

    @CompileStatic
    void 'should be able to wire Simple and Matrix Jobs'() {
        when:
            JobComponent<MatrixJob> applicable = new AbstractJobComponent() & new MatrixJobComponent()
        then:
            applicable
    }

    @CompileStatic
    void 'should be able to wire Simple and Multi Jobs'() {
        when:
            JobComponent<MultiJob> applicable = new AbstractJobComponent() & new MultiJobComponent()
        then:
            applicable
    }

    @CompileStatic
    void 'should be able to wire many Simple and Multi Jobs'() {
        when:
            JobComponent<MultiJob> applicable = new AbstractJobComponent() &
                    new AbstractJobComponent() &
                    new AbstractJobComponent() &
                    new AbstractJobComponent() &
                    new MultiJobComponent() &
                    new MultiJobComponent() &
                    new MultiJobComponent() &
                    new MultiJobComponent()
        then:
            applicable
    }

    void 'should propagate Job configuration application to every item in composite'() {
        given:
            JobComponent<Job> applicableItem = Spy(AbstractJobComponent)
            Job job = Mock()
        and:
            JobComponent<Job> applicable = applicableItem &
                    applicableItem &
                    applicableItem &
                    applicableItem &
                    applicableItem

        when:
            applicable.applyOn(job)

        then:
            5 * applicableItem.applyOn(job)
    }

    void 'should propagate Job configuration to each of the components in defined order'() {
        given:
            JobComponent<Job> firstComponent = Spy(AbstractJobComponent)
            JobComponent<Job> secondComponent = Spy(AbstractJobComponent)
            JobComponent<Job> thirdComponent = Spy(AbstractJobComponent)
            Job job = Mock()
        and:
            JobComponent<Job> applicable = firstComponent &
                    secondComponent &
                    thirdComponent

        when:
            applicable.applyOn(job)

        then:
            1 * firstComponent.applyOn(job)
        and:
            1 * secondComponent.applyOn(job)
        and:
            1 * thirdComponent.applyOn(job)
    }

    void 'should not be able to wire incompatible Components'() {
        expect:
            with(GroovyAssert.shouldFail("""
                import com.ofg.job.component.JobComponentSpec.MatrixJobComponent
                import com.ofg.job.component.JobComponentSpec.MultiJobComponent
                import groovy.transform.CompileStatic

                @CompileStatic
                class Foo {
                                           
                    void execute() {
                        new MultiJobComponent() & new MatrixJobComponent()
                    }
                }
                
            """)) {
                it.class == MultipleCompilationErrorsException
                it.message.contains("[Static type checking]")
            }
    }

    void 'should not be able to wire Specific Job Component with Abstract Job Component'() {
        expect:
            with(GroovyAssert.shouldFail("""
                import com.ofg.job.component.JobComponentSpec.AbstractJobComponent
                import com.ofg.job.component.JobComponentSpec.MultiJobComponent
                import groovy.transform.CompileStatic

                @CompileStatic
                class Foo {
                
                    void execute() {
                        new MultiJobComponent() & new AbstractJobComponent()
                    }
                }
                
            """)) {
                it.class == MultipleCompilationErrorsException
                it.message.contains("[Static type checking]")
            }
    }

    void fooStaticTypeCheckingExceptionThrown(Throwable it) {
        assert it.class == MultipleCompilationErrorsException
        assert it.message.contains("[Static type checking]")
    }

    static class MatrixJobComponent implements JobComponent<MatrixJob> {

        @Override
        void applyOn(MatrixJob job) {
        }
    }

    static class AbstractJobComponent implements JobComponent<Job> {

        @Override
        void applyOn(Job job) {
        }
    }

    static class MultiJobComponent implements JobComponent<MultiJob> {

        @Override
        void applyOn(MultiJob job) {
        }
    }
}
