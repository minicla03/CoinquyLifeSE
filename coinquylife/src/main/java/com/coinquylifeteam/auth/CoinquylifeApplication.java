package com.coinquylifeteam.auth;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
@SpringBootApplication
public class CoinquylifeApplication extends ResourceConfig
{
	public CoinquylifeApplication()
	{
		packages("com.coinquylifeteam.auth.Controller");
	}

	public static void main(String[] args) {
		SpringApplication.run(CoinquylifeApplication.class, args);
	}

}
