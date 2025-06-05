package com.coiquyteam.rank;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = "com.coiquyteam.rank")
@ApplicationPath("/rest")
@EnableMongoRepositories(basePackages = {
        "com.coiquyteam.rank.Repository",
        "com.coinquyteam.shift.Repository"
})
public class RankApplication extends ResourceConfig
{
    public RankApplication()
    {
        packages("Controller");
        packages("Service");
        packages("Repository");
    }
    public static void main(String[] args) {
        SpringApplication.run(RankApplication.class, args);
    }
}
