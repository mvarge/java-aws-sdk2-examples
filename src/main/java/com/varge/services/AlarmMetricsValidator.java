package com.varge.services;

import com.google.inject.Inject;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.DescribeAlarmsRequest;
import software.amazon.awssdk.services.cloudwatch.model.DimensionFilter;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsRequest;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsResponse;
import software.amazon.awssdk.services.cloudwatch.model.MetricAlarm;
import software.amazon.awssdk.services.cloudwatch.paginators.DescribeAlarmsIterable;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmMetricsValidator {

    private final CloudWatchClient cloudWatchClient;

    @Inject
    public AlarmMetricsValidator(CloudWatchClient cloudWatchClient) {
        this.cloudWatchClient = cloudWatchClient;
    }

    public void validateAllAlarms() {
        DescribeAlarmsRequest request = DescribeAlarmsRequest.builder().build();
        DescribeAlarmsIterable responses = cloudWatchClient.describeAlarmsPaginator(request);

        responses.stream().forEach(
                response -> response.metricAlarms()
                        .forEach(alarm -> {
                            if (alarmHasMetrics(alarm)) {
                                takeAction(alarm);
                            }
                        }));
    }

    public boolean alarmHasMetrics(MetricAlarm alarm) {
        List<DimensionFilter> filter = alarm.dimensions().stream()
                .map(dimension ->
                    DimensionFilter.builder()
                            .name(dimension.name())
                            .value(dimension.value())
                            .build())
                .collect(Collectors.toList());
        ListMetricsRequest request = ListMetricsRequest.builder()
                .metricName(alarm.metricName())
                .namespace(alarm.namespace())
                .dimensions(filter)
                .build();

        ListMetricsResponse response = cloudWatchClient.listMetrics(request);

        return response.hasMetrics();
    }

    private void takeAction(MetricAlarm alarm) {
        System.out.printf("Some action with %s%n", alarm.alarmName());
    }

}
