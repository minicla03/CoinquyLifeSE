package com.coinquyteam.dashboard;

import jakarta.ws.rs.ApplicationPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ApplicationPath("/rest")
@SpringBootApplication
public class DashApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(DashApplication.class, args);
    }
}
