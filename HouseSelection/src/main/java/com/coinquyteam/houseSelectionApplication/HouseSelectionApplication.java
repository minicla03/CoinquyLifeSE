package com.coinquyteam.houseSelectionApplication;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ApplicationPath("/rest")
public class HouseSelectionApplication extends ResourceConfig
{
    public HouseSelectionApplication()
    {
        packages("com/coinquyteam/houseSelectionApplication/Controller");
        packages("com/coinquyteam/houseSelectionApplication/Service");
        packages("com/coinquyteam/houseSelectionApplication/Data");
        packages("com/coinquyteam/houseSelectionApplication/JWT");
        packages("com/coinquyteam/houseSelectionApplication/Config");
    }
    public static void main(String[] args)
    {
        SpringApplication.run(HouseSelectionApplication.class, args);
    }
}
