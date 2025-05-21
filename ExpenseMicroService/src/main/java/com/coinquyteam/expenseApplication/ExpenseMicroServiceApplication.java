package com.coinquyteam.expenseApplication;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ApplicationPath("/rest")
@EnableMongoRepositories
public class ExpenseMicroServiceApplication extends ResourceConfig
{
    public ExpenseMicroServiceApplication()
    {

    }

    public static void main(String[] args)
    {
        SpringApplication.run(ExpenseMicroServiceApplication.class, args);
    }
}
