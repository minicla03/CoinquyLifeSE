package com.coinquyteam.dashboard;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ApplicationPath("/rest")
@SpringBootApplication(scanBasePackages = "com.coinquyteam.dashboard")
public class DashApplication extends ResourceConfig
{
    public DashApplication()
    {
        packages("com.coinquyteam.dashboard.Config");
        packages("com.coinquyteam.dashboard.Controller");
        packages("com.coinquyteam.dashboard.Service");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(DashApplication.class, args);
    }
}
