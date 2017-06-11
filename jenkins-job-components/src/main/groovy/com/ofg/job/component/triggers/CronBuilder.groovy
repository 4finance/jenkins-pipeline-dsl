package com.ofg.job.component.triggers

import groovy.transform.CompileStatic
import groovy.transform.PackageScope

/**
 * This class grants dsl support for constructing cron strings, that are mostly used
 * for CI systems. It allows definitions as:
 *
 * defineAs {
 *    every 5 minutes()
 * }
 *
 * defineAs {
 *     at midnight()
 * }
 *
 * all of the above is checked at compilation time
 *
 * @author Szymon Homa
 * @author Marek Kapowicki
 * @author Adam Wojszczyk
 */
class CronBuilder {
    
    private CronExpression cronExpression
    private TimeUnitBuilder timeUnitBuilder

    static String generateAs(@DelegatesTo(CronBuilder) Closure setup) {
        return defineAs(setup)
                .build()
    }

    static CronBuilder defineAs(@DelegatesTo(CronBuilder) Closure setup) {
        CronBuilder builder = new CronBuilder();
        builder.with(setup)
        return builder
    }

    TimeUnitBuilder every(int value) {
        this.timeUnitBuilder = new TimeUnitBuilder(value);
        return timeUnitBuilder;
    }

    void at(CronExpression expression) {
        this.cronExpression = expression
    }

    @PackageScope
    String build() {
        List<CronExpressionFactory> factories = [cronExpression, timeUnitBuilder]
        return factories.stream()
                .filter({ it != null })
                .findFirst()
                .map({ it.buildExpression() })
                .orElseThrow({ new IllegalStateException('unspecified cron builder: no valid CronExpressionFactory') })
    }

    static class TimeUnitBuilder implements CronExpressionFactory {

        private List<String> cronTemplate = ['H', '*', '*', '*', '*']
        private final int value
        private int unitKey = 1

        TimeUnitBuilder(int value) {
            if (value < 1) {
                throw new IllegalArgumentException("value ${this.value} for minutes or hours is too low")
            }
            this.value = value
        }

        void minutes() {
            this.unitKey = 0
        }

        void hours() {
            this.unitKey = 1
        }

        @Override
        String buildExpression() {
            applyUnitsOnTemplate()
            return cronTemplate.join(' ');
        }

        private String applyUnitsOnTemplate() {
            cronTemplate.set(unitKey, "H/$value")
        }
    }

    static class CronExpression implements CronExpressionFactory {

        private String cronExpression
    
        CronExpression(String cronExpression) {
            this.cronExpression = cronExpression
        }
    
        static CronExpression midnight() {
            return new CronExpression('@midnight')
        }

        static CronExpression everyHour() {
            return new CronExpression('@hourly')
        }

        static CronExpression hour(int hour) {
            return new CronExpression("H $hour * * *")
        }

        @Override
        String buildExpression() {
            return cronExpression
        }
    }

    private static interface CronExpressionFactory {

        String buildExpression()
    }
}
