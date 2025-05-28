package com.coinquyteam.expense;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.coinquyteam.expense")
@ApplicationPath("/rest")
public class ExpenseMicroServiceApplication extends ResourceConfig
{
	public ExpenseMicroServiceApplication()
	{
		packages("com.coinquyteam.expense.Controller");
		packages("Data");
		packages("Service");
		packages("Repository");
		packages("Utility");
	}

	public static void main(String[] args)
	{
		SpringApplication.run(ExpenseMicroServiceApplication.class, args);
	}
}
