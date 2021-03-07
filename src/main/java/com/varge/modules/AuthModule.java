package com.varge.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

public class AuthModule extends AbstractModule {

    private final Region region;
    private final String account;
    /**
     * Role to assume with STS client
     */
    private final String arnRoleTemplate = "arn:aws:iam::%s:role/CloudwatchTestRole";

    @Override
    protected void configure() {
        install(new CloudwatchModule());
    }

    public AuthModule(Region region, String account) {
        this.region = region;
        this.account = account;
    }

    @Provides
    Region getRegion() {
        return region;
    }

    @Provides
    @Named("arnRole")
    String getArnRole() {
        return String.format(arnRoleTemplate, account);
    }

    /**
     * In the case we want to manipulate the default credentials provider behavior, load our own credentials
     * from some other place, etc.
     */
    @Provides
    @Singleton
    AwsCredentialsProvider getAwsCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    @Provides
    @Singleton
    StsClient getStsClient(AwsCredentialsProvider credentialsProvider, Region region) {
        return StsClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Provides
    @Singleton
    StsAssumeRoleCredentialsProvider getStsAssumeRoleCredentialsProvider(
            StsClient stsClient,
            @Named("arnRole") String arnRole) {
        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(arnRole)
                .roleSessionName("MY_SESSION_TOKEN")
                .build();

        return StsAssumeRoleCredentialsProvider.builder()
                .stsClient(stsClient)
                .refreshRequest(assumeRoleRequest)
                .build();
    }
}
