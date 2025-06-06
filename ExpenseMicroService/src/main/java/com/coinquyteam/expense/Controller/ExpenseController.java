package com.coinquyteam.expense.Controller;

import com.coinquyteam.expense.Data.CategoryExpense;
import com.coinquyteam.expense.Data.Expense;
import com.coinquyteam.expense.Service.ExpenseService;
import com.coinquyteam.expense.Utility.ExpenseDebtResult;
import com.coinquyteam.expense.Utility.ExpenseResult;
import com.coinquyteam.expense.Utility.ExpenseStatus;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;
import java.util.Map;


@Path("/expense")
@Consumes("application/json")
@Produces("application/json")
public class ExpenseController
{
    @Autowired private ExpenseService expenseService;

    @POST
    @Path("/createExpense")
    public Response createExpense(Expense expense)
    {
        //System.out.println("Creating expense: " + expense);
        String expenseDescription = expense.getDescription();
        Date expenseDate = new Date(); // Assuming the current date is used for the expense
        double expenseAmount = expense.getAmount();
        String createdBy = expense.getCreatedBy();
        String houseId = expense.getHouseId();
        CategoryExpense category = expense.getCategory();
        List<String> partecipants= expense.getParticipants();


        ExpenseResult expenseResult =expenseService.createExpense(expenseDescription, expenseDate, expenseAmount, houseId, createdBy, category, partecipants);

        if (expenseResult.getStatus() == ExpenseStatus.SUCCESS)
        {
            return Response.ok(expenseResult).build();
        }
        else if (expenseResult.getStatus() == ExpenseStatus.ERROR)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(expenseResult).build();
        }
        else if (expenseResult.getStatus() == ExpenseStatus.INVALID_INPUT){

            return Response.status(Response.Status.BAD_REQUEST).entity(expenseResult).build();
        }
        else
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(expenseResult).build();
        }
    }

    @GET
    @Path("/getAllExpenses")
    public Response getAllExpenses(@QueryParam("houseId") String houseId)
    {
        ExpenseResult expenseResult=expenseService.getAllExpenses(houseId);
        if (expenseResult.getStatus() == ExpenseStatus.SUCCESS)
        {
            return Response.ok(expenseResult).build();
        }
        else if (expenseResult.getStatus() == ExpenseStatus.NO_CONTENT)
        {
            return Response.status(Response.Status.NO_CONTENT).entity(expenseResult).build();
        }
        else
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }

    @POST
    @Path("/calculateDebt")
    public Response calculateDebt(Map<String, String> requestBody)
    {
        String houseId = requestBody.get("houseId");
        ExpenseDebtResult expenseResult = expenseService.calculateDebt(houseId);

        if (expenseResult.getStatus() == ExpenseStatus.SUCCESS)
        {
            return Response.ok(expenseResult).build();
        }
        else if (expenseResult.getStatus() == ExpenseStatus.NO_CONTENT)
        {
            return Response.status(Response.Status.NOT_FOUND).entity(expenseResult).build();
        }
        else
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }
    @POST
    @Path("/updateExpenseStatus")
    public Response updateExpenseStatus(Map<String, String> requestBody) {
        try {
            String expenseId = requestBody.get("expenseId");
            ExpenseResult expenseResult = expenseService.updateExpenseStatus(expenseId);
            if (expenseResult.getStatus() == ExpenseStatus.SUCCESS) {
                return Response.ok(expenseResult).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(expenseResult).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating expense status: " + e.getMessage()).build();
        }
    }
}
