package com.coinquyteam.dashboard.Controller;

import com.coinquyteam.dashboard.Service.DashService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/dash")
@Produces(MediaType.APPLICATION_JSON)
public class DashController
{
    @Autowired
    private DashService dashService;

    @GET
    @Path("/retrieveCoinquy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoinquy(@QueryParam("houseId") String houseId) {
        return Response.ok(dashService.getCoinquy(houseId)).build();
    }

    @GET
    @Path("/retrieveTurni")
    public Response getTurni(@QueryParam("houseId") String houseId)
    {
        return Response.ok(dashService.getTurni(houseId)).build();
    }

    @GET
    @Path("/retrieveClassifica")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClassifica(@QueryParam("houseId") String houseId)
    {
        return Response.ok(dashService.getClassifica(houseId)).build();
    }
}
