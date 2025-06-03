package com.coinquyteam.dashboard;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ApplicationPath("/rest")
@SpringBootApplication
public class DashApplication extends ResourceConfig
{
    public void register()
    {
        packages("Controller");
        packages("Service");
        packages("Config");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(DashApplication.class, args);
    }
}
