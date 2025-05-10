package com.coinquylifeteam.auth;

import com.coinquylifeteam.auth.Controller.AuthController;
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
		register(new AuthController());
	}

	public static void main(String[] args) {
		SpringApplication.run(CoinquylifeApplication.class, args);
	}

}
