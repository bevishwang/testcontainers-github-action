package com.citadel.csds.oss.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class GcsTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final int FAKE_GCS_SERVER_PORT = 4443;
    private static final String FAKE_GCS_SERVER_IMAGE = "fsouza/fake-gcs-server";

    private GenericContainer<?> container;

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        this.container = createContainer();
        this.container.start();

        applyConfiguration(configurableApplicationContext);

        configurableApplicationContext.addApplicationListener(new ContainerShutdownListener());
    }

    private class ContainerShutdownListener implements ApplicationListener<ContextClosedEvent> {
        @Override
        public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
            if (container.isRunning()) {
                container.stop();
            }
        }
    }

    private GenericContainer<?> createContainer() {
        return new GenericContainer<>(DockerImageName.parse(FAKE_GCS_SERVER_IMAGE))
            .withExposedPorts(FAKE_GCS_SERVER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("/bin/fake-gcs-server", "-scheme", "http"));
    }

    private void applyConfiguration(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of("gcs.port=" + container.getFirstMappedPort())
                          .applyTo(configurableApplicationContext.getEnvironment());
    }
}
