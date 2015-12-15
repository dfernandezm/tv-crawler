package com.morenware.tvcrawler.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.IsolationLevelDataSourceAdapter;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionDefinition;

import java.util.Properties;

@Configuration
@ImportResource({"classpath:jdbc-local.xml", "classpath:configurable-context.xml"})
public class MainDatabaseConfig  {

    @Autowired
    private ComboPooledDataSource targetDataSource;

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }


//    @Bean
//    public IsolationLevelDataSourceAdapter dataSource() {
//        IsolationLevelDataSourceAdapter dataSource = new IsolationLevelDataSourceAdapter();
//        dataSource.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
//        dataSource.setTargetDataSource(targetDataSource);
//        return dataSource;
//    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(targetDataSource);
        sessionFactory.setMappingDirectoryLocations(new ClassPathResource("hibernate-mappings"));
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    public Properties hibernateProperties() {
        return new Properties() {
            private static final long serialVersionUID = 1L;

            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
                setProperty("hibernate.show_sql", "false");
                setProperty("hibernate.hbm2ddl.auto", "validate");
            }
        };
    }


}
