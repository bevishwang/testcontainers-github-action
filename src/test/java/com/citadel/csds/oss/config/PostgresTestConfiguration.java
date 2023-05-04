package com.citadel.csds.oss.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class PostgresTestConfiguration implements InitializingBean, DisposableBean {

    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2")
            .withInitScript("init.sql")
            .withExposedPorts(5432)
            .withAccessToHost(true)
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @Bean
    public DataSource dataSource() {
        var config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        config.setDriverClassName(postgres.getDriverClassName());
        config.setMaximumPoolSize(1);
        config.setConnectionTimeout(1000);
        config.setValidationTimeout(1000);
        config.setPoolName("test");
        config.setConnectionTestQuery("SELECT 1");
        config.setInitializationFailTimeout(1000);
        config.setLeakDetectionThreshold(1000);
        config.setRegisterMbeans(true);
        config.setAllowPoolSuspension(true);
        config.setReadOnly(false);
        config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

        return new HikariDataSource(config);
    }

    @Override
    public void destroy() {
        postgres.stop();
    }

    @Override
    public void afterPropertiesSet() {
        postgres.start();
    }
}
