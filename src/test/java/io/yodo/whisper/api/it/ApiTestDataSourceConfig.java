package io.yodo.whisper.api.it;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
public class ApiTestDataSourceConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    @Primary // api app itself has no DS, this is only loaded by test classes
    @ConfigurationProperties(prefix="app.datasource.backend")
    public DataSourceProperties backendDataSourceProps() {
        return new DataSourceProperties();
    }

    @Bean(name = "backendDataSource")
    @Primary
    public DataSource backendDS(DataSourceProperties props) {
        logger.debug("Creating datasource for url " + props.getUrl());
        return props.initializeDataSourceBuilder().type(DriverManagerDataSource.class).build();
    }
}
