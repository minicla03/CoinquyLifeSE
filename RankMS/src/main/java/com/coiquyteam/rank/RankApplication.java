package com.coiquyteam.rank;

import com.coiquyteam.rank.Controller.ClientRankController;
import com.coiquyteam.rank.Controller.RankController;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = "com.coiquyteam.rank")
@ApplicationPath("/rest")
@EnableMongoRepositories(basePackages = {
        "com.coiquyteam.rank.Repository",
})
public class RankApplication extends ResourceConfig
{
    public RankApplication()
    {
        packages("Controller");
        //packages("com.coiquyteam.rank.Controller");
        register(RankController.class);
        register(ClientRankController.class);
        packages("Service");
        packages("Repository");
    }
    public static void main(String[] args) {
        SpringApplication.run(RankApplication.class, args);
    }
}
