package com.coinquyteam.authApplication;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;;

@SpringBootApplication
@ApplicationPath("/rest")
public class AuthMicroServiceApplication extends ResourceConfig
{
    public AuthMicroServiceApplication()
    {
        packages("com/coinquyteam/authApplication/Controller");
        packages("com/coinquyteam/authApplication/Service");
        packages("com/coinquyteam/authApplication/Data");
        packages("com/coinquyteam/authApplication/JWT");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(AuthMicroServiceApplication.class, args);
    }
}
