package com.coinquyteam.shift;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ApplicationPath("/rest")
public class ShiftApplication extends ResourceConfig
{
    public ShiftApplication()
    {
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
