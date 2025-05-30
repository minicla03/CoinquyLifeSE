package com.coinquyteam.shift;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ApplicationPath("/rest")
@EnableMongoRepositories(basePackages = "com.coinquyteam.shift.Repository")
public class ShiftApplication extends ResourceConfig
{
    public ShiftApplication()
    {
        packages("Config");
        packages("Controller");
        packages("Service");
        packages("Data");
        packages("OptaPlanner");
        packages("Repository");
    }

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ShiftApplication.class, args);
    }
}
