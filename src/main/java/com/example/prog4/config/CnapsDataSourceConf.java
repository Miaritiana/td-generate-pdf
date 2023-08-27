package com.example.prog4.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
        basePackages = "com.example.prog4.repository.cnaps",
        entityManagerFactoryRef = "cnapsEntityManager",
        transactionManagerRef = "cnapsTransactionManager"
)
public class CnapsDataSourceConf {
    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean cnapsEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(cnapsDataSource());
        em.setPackagesToScan(
                new String[] { "com.example.prog4.repository.entity.cnaps" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource cnapsDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("cnaps.jdbc.url"));
        dataSource.setUsername(env.getProperty("cnaps.jdbc.user"));
        dataSource.setPassword(env.getProperty("cnaps.jdbc.pass"));

        return dataSource;
    }

    @Bean
    public Flyway cnapsFlyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(cnapsDataSource())
                .locations("classpath:/db/migration/cnaps")
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public PlatformTransactionManager cnapsTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                cnapsEntityManager().getObject());
        return transactionManager;
    }
}