package com.coinquyteam.dashboardms.Controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/client")
public class ClientController
{
    @GET
    @Path("/spese")
    @Produces(MediaType.TEXT_HTML)
    public Response redirectExpense()
    {
        return Response.ok("{\"path\": \"http://localhost:8084/Expense/expensePage.html\"}", MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/turni")
    @Produces(MediaType.TEXT_HTML)
    public Response redirectShift()
    {
        return Response.ok("{\"path\": \"http://localhost:8085/Shift/shiftPage.html\"}", MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/regole")
    @Produces(MediaType.TEXT_HTML)
    public Response redirectRule()
    {
        return Response.ok("{\"path\": \"http://localhost:8086/Rule/rulePage.html\"}", MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/classifica")
    @Produces(MediaType.TEXT_HTML)
    public Response redirectRank()
    {
        return Response.ok("{\"path\": \"http://localhost:8087/Rank/rankPage.html\"}", MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/profilo")
    @Produces(MediaType.TEXT_HTML)
    public Response redirectProfile()
    {
        return Response.ok("{\"path\": \"http://localhost:8088/Profile/profilePage.html\"}", MediaType.APPLICATION_JSON).build();
    }

}