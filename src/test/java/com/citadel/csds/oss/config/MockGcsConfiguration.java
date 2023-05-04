package com.citadel.csds.oss.config;

import com.citadel.csds.oss.GcsFileStore;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockGcsConfiguration {

    private static final String TEST_BUCKET_NAME = "test-bucket";

    private Storage storageClient(String fakeGcsServerMappedPort) {
        return StorageOptions.newBuilder()
                             .setHost("http://localhost:" + fakeGcsServerMappedPort)
                             .setProjectId("test-project")
                             .build()
                             .getService();
    }

    @Bean
    public GcsFileStore gcs(@Value("${gcs.port}") String fakeGcsServerMappedPort) {
        var storage = storageClient(fakeGcsServerMappedPort);
        if (storage.get(TEST_BUCKET_NAME) == null) {
            storage.create(BucketInfo.of(TEST_BUCKET_NAME));
        }

        return new GcsFileStore(
            storage,
            TEST_BUCKET_NAME
        );
    }
}
