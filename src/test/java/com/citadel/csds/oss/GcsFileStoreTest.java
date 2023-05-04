package com.citadel.csds.oss;

import com.citadel.csds.oss.config.GcsTestContainerInitializer;
import com.citadel.csds.oss.config.MockGcsConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {MockGcsConfiguration.class})
@ContextConfiguration(initializers = GcsTestContainerInitializer.class)
public class GcsFileStoreTest extends FileStoreTest {

    @Override
    public void givenFile_whenUploadAndAccessSignedUrl_thenSuccess() {
        //Disable this test because it is not supported by fake-gcs-server
    }
}
