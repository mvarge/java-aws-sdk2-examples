package com.varge.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;

public class CloudwatchModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    CloudWatchClient getCloudwatchClient(StsAssumeRoleCredentialsProvider stsAssumeRoleCredentialsProvider,
                                         Region region) {
        return CloudWatchClient.builder()
                .region(region)
                .credentialsProvider(stsAssumeRoleCredentialsProvider)
                .build();
    }

}