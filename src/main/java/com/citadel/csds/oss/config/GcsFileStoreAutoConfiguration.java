package com.citadel.csds.oss.config;

import com.citadel.csds.oss.FileStore;
import com.citadel.csds.oss.GcsFileStore;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.IOException;

@AutoConfiguration
@ConditionalOnClass(Storage.class)
@ConditionalOnProperty(prefix = "oss", name = "provider", havingValue = "gcs")
@EnableConfigurationProperties(GcsFileStoreProperties.class)
public class GcsFileStoreAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(GcsFileStoreAutoConfiguration.class);

    private final GcsFileStoreProperties gcsFileStoreProperties;

    public GcsFileStoreAutoConfiguration(GcsFileStoreProperties gcsFileStoreProperties) {
        this.gcsFileStoreProperties = gcsFileStoreProperties;
    }

    private Storage buildStorageClient() throws IOException {
        log.info("Registering Google Storage client");

        var credentials = ServiceAccountCredentials.fromStream(new FileInputStream(this.gcsFileStoreProperties.keyfile()));
        return StorageOptions.newBuilder()
                             .setCredentials(credentials)
                             .build()
                             .getService();
    }

    @Bean
    @ConditionalOnMissingBean
    public FileStore gcs() throws IOException {
        log.info("Registering Google Cloud Storage File Store");

        return new GcsFileStore(buildStorageClient(), this.gcsFileStoreProperties.bucket());
    }
}
