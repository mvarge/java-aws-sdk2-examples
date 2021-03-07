package com.varge.samples;

import com.google.inject.Inject;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.DescribeAlarmsRequest;
import software.amazon.awssdk.services.cloudwatch.paginators.DescribeAlarmsIterable;

public class AlarmMetricsValidator {

    private final CloudWatchClient cloudWatchClient;

    @Inject
    public AlarmMetricsValidator(CloudWatchClient cloudWatchClient) {
        this.cloudWatchClient = cloudWatchClient;
    }

    public void listAllAlarms() {
        DescribeAlarmsRequest request = DescribeAlarmsRequest.builder().build();
        DescribeAlarmsIterable responses = cloudWatchClient.describeAlarmsPaginator(request);

        responses.stream().forEach((a) -> System.out.println(a));
    }

}
