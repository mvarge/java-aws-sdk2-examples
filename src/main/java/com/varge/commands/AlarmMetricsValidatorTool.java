package com.varge.commands;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.varge.modules.AuthModule;
import com.varge.samples.AlarmMetricsValidator;
import software.amazon.awssdk.regions.Region;

public class AlarmMetricsValidatorTool {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AuthModule(Region.US_WEST_2, "89385375875"));
        AlarmMetricsValidator validator = injector.getInstance(AlarmMetricsValidator.class);

        validator.listAllAlarms();
    }

}
