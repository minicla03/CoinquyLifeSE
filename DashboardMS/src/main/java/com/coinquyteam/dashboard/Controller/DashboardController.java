package com.coinquyteam.dashboard.Controller;

import com.coinquyteam.dashboard.Service.DashboardService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("dashboard")
public class DashboardController
{
    @Autowired
    private DashboardService dashboardService;

    @GET
    @Path("/data")
    public Response getDashboardData()
    {

    }
}
