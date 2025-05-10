package com.coinquyteam.coinquylife;

import com.coinquylifeteam.auth.Controller.AuthController;;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.coinquylifeteam.auth.Repository")
@ApplicationPath("/rest")
public class CoinquylifeApplication extends ResourceConfig
{
	public CoinquylifeApplication()
	{
		register(new AuthController());
	}
	public static void main(String[] args) {
		SpringApplication.run(CoinquylifeApplication.class, args);
	}

}
