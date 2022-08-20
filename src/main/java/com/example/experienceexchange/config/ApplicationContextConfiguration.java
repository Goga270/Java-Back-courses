package com.example.experienceexchange.config;

import com.example.experienceexchange.model.Account;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.IUserRepository;
import com.example.experienceexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.example.experienceexchange"})
@PropertySource("classpath:/application.properties")
@Import(SecurityConfig.class)
@EnableTransactionManagement
public class ApplicationContextConfiguration {

    @Value("${spring.dataSource.password}")
    private String PASSWORD;
    @Value("${spring.dataSource.username}")
    private String USERNAME;
    @Value("${spring.dataSource.driver.class.name}")
    private String DRIVER_CLASS_NAME;
    @Value("${spring.dataSource.url}")
    private String URL;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String FORMAT_SQL;
    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String SHOW_SQL;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String DIALECT;
    @Value("${spring.jpa.properties.packagesToScan}")
    private String PACKAGES_TO_SCAN;
    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String DDL_AUTO;

    @Bean
    public IUserRepository userRepository() {
        UserRepository userRepository = new UserRepository();
        userRepository.setClass(User.class);
        return userRepository;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername(USERNAME);
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setUrl(URL);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan(PACKAGES_TO_SCAN);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(additionalProperties());
        return entityManagerFactory;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", SHOW_SQL);
        properties.setProperty("hibernate.dialect", DIALECT);
        properties.setProperty("hibernate.format_sql", FORMAT_SQL);
        properties.setProperty("hibernate.hdm2ddl.auto",DDL_AUTO);
        return properties;
    }
}
