package com.coinquyteam.dashboardms;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ApplicationPath("/rest")
public class DashboardMsApplication extends ResourceConfig {

    public DashboardMsApplication() {
        packages("com.coinquyteam.dashboardms.Controller");
    }

    public static void main(String[] args) {
        SpringApplication.run(DashboardMsApplication.class, args);
    }

}
