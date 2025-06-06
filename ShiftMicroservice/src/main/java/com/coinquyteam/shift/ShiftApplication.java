package com.coinquyteam.shift;

import com.coinquyteam.shift.Controller.*;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.coinquyteam.shift")
@ApplicationPath("/rest")
@EnableMongoRepositories(basePackages = "com.coinquyteam.shift.Repository")
public class ShiftApplication extends ResourceConfig
{
    public ShiftApplication()
    {
        packages("Config");
        register(UnAvailabilityController.class);
        register(HouseTaskController.class);
        register(SwapController.class);
        register(CalendarController.class);
        register(ClientShiftController.class);
        packages("com.coinquyteam.shift.Service");
        packages("Data");
        packages("OptaPlanner");
        packages("Repository");
    }

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ShiftApplication.class, args);
    }
}
