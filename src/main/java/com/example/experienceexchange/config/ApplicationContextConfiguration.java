package com.example.experienceexchange.config;

import com.example.experienceexchange.model.*;
import com.example.experienceexchange.repository.*;
import com.example.experienceexchange.repository.filter.IFilterProvider;
import com.example.experienceexchange.repository.filter.IPredicateBuilder;
import com.example.experienceexchange.repository.filter.JpqlFilterProvider;
import com.example.experienceexchange.repository.filter.PredicateBuilder;
import com.example.experienceexchange.repository.interfaceRepo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.example.experienceexchange"})
@PropertySource("classpath:/application.properties")
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
    @Value("${spring.timezone}")
    private String TIME_ZONE;

    @Bean
    public TimeZone timeZone() {
        log.info("Selected time zone = {}", TIME_ZONE);
        TimeZone defaultTimeZone = TimeZone.getTimeZone(TIME_ZONE);
        TimeZone.setDefault(defaultTimeZone);
        return defaultTimeZone;
    }

    @Bean
    public IFilterProvider courseFilter() {
        List<String> parameters = new LinkedList<>();
        parameters.add("section.id");
        parameters.add("direction.id");
        parameters.add("skill.id");
        parameters.add("course.masteryLevel");
        return new JpqlFilterProvider(parameters, predicateBuilder());
    }

    @Bean
    public IFilterProvider lessonFilter() {
        List<String> parameters = new LinkedList<>();
        parameters.add("section.id");
        parameters.add("direction.id");
        parameters.add("skill.id");
        parameters.add("lesson.masteryLevel");
        return new JpqlFilterProvider(parameters, predicateBuilder());
    }

    @Bean
    public IPredicateBuilder predicateBuilder() {
        return new PredicateBuilder();
    }

    @Bean
    public IPaymentRepository paymentRepository() {
        PaymentRepository paymentRepository = new PaymentRepository();
        paymentRepository.setClass(Payment.class);
        return paymentRepository;
    }

    @Bean
    public ILessonOnCourseRepository lessonOnCourseRepository() {
        LessonOnCourseRepository lessonOnCourseRepository = new LessonOnCourseRepository();
        lessonOnCourseRepository.setClass(LessonOnCourse.class);
        return lessonOnCourseRepository;
    }

    @Bean
    public ICommentRepository commentRepository() {
        CommentRepository commentRepository = new CommentRepository();
        commentRepository.setClass(Comment.class);
        return commentRepository;
    }

    @Bean
    public ICourseRepository courseRepository() {
        CourseRepository courseRepository = new CourseRepository();
        courseRepository.setClass(Course.class);
        return courseRepository;
    }

    @Bean
    public ILessonRepository lessonRepository() {
        LessonRepository lessonRepository = new LessonRepository();
        lessonRepository.setClass(LessonSingle.class);
        return lessonRepository;
    }

    @Bean
    public ISectionRepository sectionRepository() {
        SectionRepository sectionRepository = new SectionRepository();
        sectionRepository.setClass(Section.class);
        return sectionRepository;
    }

    @Bean
    public IDirectionRepository directionRepository() {
        DirectionRepository directionRepository = new DirectionRepository();
        directionRepository.setClass(Direction.class);
        return directionRepository;
    }

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
        log.info("Created JPA transaction manager");
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
        log.info("Selected data source for database: username: {},driver class name: {}, password: {},url: {}", USERNAME, DRIVER_CLASS_NAME, PASSWORD, URL);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan(PACKAGES_TO_SCAN);
        log.info("Created entityManagerFactory");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(additionalProperties());
        log.info("Created JpaVendorAdapter");
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
        return properties;
    }
}
