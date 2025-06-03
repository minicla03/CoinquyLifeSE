package com.coiquyteam.rank;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@ApplicationPath("/rest")
public class RankApplication extends ResourceConfig
{
    public RankApplication()
    {
        packages("com.coiquyteam.rank.Controller");
        packages("com.coiquyteam.rank.Service");
        packages("com.coiquyteam.rank.Repository");
    }
    public static void main(String[] args) {
        SpringApplication.run(RankApplication.class, args);
    }
}
