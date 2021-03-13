import com.varge.services.AlarmMetricsValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.DimensionFilter;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsRequest;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsResponse;
import software.amazon.awssdk.services.cloudwatch.model.Metric;
import software.amazon.awssdk.services.cloudwatch.model.MetricAlarm;

public class AlarmMetricsValidatorTest {

    private CloudWatchClient cloudWatchClient;
    private AlarmMetricsValidator validator;

    @Before
    public void setup() {
        cloudWatchClient = Mockito.mock(CloudWatchClient.class);
        validator = new AlarmMetricsValidator(cloudWatchClient);
    }

    @Test
    public void testAlarmHasMetrics() {
        MetricAlarm alarm = MetricAlarm.builder()
                .namespace("SomeNamespace")
                .metricName("SomeMetricName")
                .build();
        ListMetricsRequest request = ListMetricsRequest.builder()
                .metricName(alarm.metricName())
                .namespace(alarm.namespace())
                .dimensions(DimensionFilter.builder().build())
                .build();
        Mockito.when(cloudWatchClient.listMetrics(request)).thenReturn(ListMetricsResponse.builder()
                .metrics(Metric.builder().build())
                .build());

        boolean result = validator.alarmHasMetrics(alarm);
        Assert.assertTrue(result);
    }
}
