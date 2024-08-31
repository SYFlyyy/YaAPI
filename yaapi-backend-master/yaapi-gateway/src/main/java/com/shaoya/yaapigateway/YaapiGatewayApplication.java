package com.shaoya.yaapigateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo
public class YaapiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YaapiGatewayApplication.class, args);
    }

//    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(YaapiGatewayApplication.class, args);
//        YaapiGatewayApplication application = context.getBean(YaapiGatewayApplication.class);
//        String result = application.doSayHello("shaoyafan");
//        System.out.println("result: " + result);
//    }

}
