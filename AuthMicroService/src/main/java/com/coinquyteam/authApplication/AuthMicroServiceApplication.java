package com.coinquyteam.authApplication;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;;

@SpringBootApplication(scanBasePackages = "com.coinquyteam.authApplication")
@ApplicationPath("/rest")
public class AuthMicroServiceApplication extends ResourceConfig
{
    public AuthMicroServiceApplication()
    {
        packages("com/coinquyteam/authApplication/Controller");
        packages("com/coinquyteam/authApplication/Service");
        packages("com/coinquyteam/authApplication/Data");
        packages("com/coinquyteam/authApplication/JWT");
        packages("com/coinquyteam/authApplication/Config");
    }

    public static void main(String[] args)
    {
        SpringApplication.run(AuthMicroServiceApplication.class, args);
    }
}
