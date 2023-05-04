package com.citadel.csds.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("oss.gcs")
public record GcsFileStoreProperties(String keyfile, String bucket) {

}
