package dk.apps.pcps.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "fp.apps.pcps.db.repository",
        entityManagerFactoryRef = "pcpsEntityManagerFactory",
        transactionManagerRef= "pcpsTransactionManager"
)
public class PCPSDBConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.pcps.datasource")
    public DataSource pcpsDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Primary
    @Bean(name = "pcpsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean pcpsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        props.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        props.put("hibernate.hbm2ddl.auto", "none");
        return builder
                .dataSource(pcpsDataSource())
                .packages("fp.apps.pcps.db.entity")
                .properties(props)
                .build();
    }

    @Primary
    @Bean(name = "pcpsTransactionManager")
    public PlatformTransactionManager pcpsTransactionManager(
            @Qualifier("pcpsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
