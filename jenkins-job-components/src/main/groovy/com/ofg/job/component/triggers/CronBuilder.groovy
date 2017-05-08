package com.ofg.job.component.triggers

import groovy.transform.PackageScope

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

    CronExpression withCronExpression() {
        this.cronExpression = new CronExpression()
        return cronExpression
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

        void midnight() {
            cronExpression = '@midnight'
        }

        void atEveryHour() {
            cronExpression = '@hourly'
        }

        void atHour(int hour) {
            cronExpression = "H $hour * * *"
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
