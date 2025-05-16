package com;

import com.microservizi.Auth.Controller.AuthController;
import com.microservizi.HouseLinking.Controller.HouseController;
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
		register(new HouseController());
		register(com.clientManager.ClientController.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CoinquylifeApplication.class, args);
	}

}
