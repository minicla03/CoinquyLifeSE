package com.coinquyteam.houseSelectionApplication;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ApplicationPath("rest")
public class HouseSelectionApplication extends ResourceConfig
{
    public HouseSelectionApplication()
    {
        packages("Controller");
        packages("Service");
        packages("Data");
        packages("Confing");
    }
    public static void main(String[] args)
    {
        SpringApplication.run(HouseSelectionApplication.class, args);
    }
}
