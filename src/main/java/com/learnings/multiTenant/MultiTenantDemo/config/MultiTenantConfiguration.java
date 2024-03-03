package com.learnings.multiTenant.MultiTenantDemo.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class MultiTenantConfiguration {
    @Value("${multidb.file.path}")
    private String path;


    @Autowired
    private Map<String, DataSourceProperties> dataSourcePropertiesMap;
    public Map<String, Properties> getProperties() {
        Map<String, Properties> map = new HashMap<>();
        File[] files = Paths.get(path).toFile().listFiles();
        log.info("path: {}", path);
        log.info("Files: {}", Arrays.asList(files));
        Arrays.asList(files).stream().filter(file -> file.getName().contains("dbconfig")).forEach(file -> {
            try {
                Properties tenantProperties = new Properties();
                tenantProperties.load(new FileInputStream(file));
                map.put(tenantProperties.getProperty("name"), tenantProperties);
            } catch (IOException e) {
                log.error("Error loading tenant files", e);
            }
        });
        return map;
    }

    @Bean
    public DataSource dataSource() {
        log.info("============DataSource::start==========");
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        getProperties()
                .forEach((tenantId, tenantProperties) -> {
                    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
                    dataSourceBuilder.driverClassName(tenantProperties.getProperty("datasource.driver-class-name"));
                    dataSourceBuilder.username(tenantProperties.getProperty("datasource.username"));
                    dataSourceBuilder.password(tenantProperties.getProperty("datasource.password"));
                    dataSourceBuilder.url(tenantProperties.getProperty("datasource.url"));
                    resolvedDataSources.put(tenantId, dataSourceBuilder.build());
                });
        MultitenantDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get("default"));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();

        log.info("============DataSource::end==========");
        return dataSource;
    }


}
