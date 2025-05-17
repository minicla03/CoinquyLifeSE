package com.SpeseMS.Controller;

import com.SpeseMS.Data.Expense;
import com.SpeseMS.Service.ExpenseService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
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
        String expenseDescription = expense.getDescription();
        Date expenseDate = expense.getCreatedDate();
        Double expenseAmount = expense.getAmount();
        String houseId = expense.getHouseId();

        return expenseService.createExpense(expenseDescription, expenseDate, expenseAmount, houseId);
    }

    @GET
    @Path("/getAllExpeses")
    public Response getAllExpenses(@QueryParam("houseId") String houseId)
    {
        return expenseService.getAllExpenses(houseId);
    }
}
