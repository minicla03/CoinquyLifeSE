package com.SpeseMS.Service;

import com.SpeseMS.Data.Expense;
import com.SpeseMS.Repository.IExpenseRepository;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExpenseService {

    @Autowired private IExpenseRepository expenseRepository;

    public Response createExpense(String expenseDescription, Date expenseDate, Double expenseAmount, String houseId)
    {
        Expense expense = new Expense(expenseDescription, expenseDate, houseId);
        Expense expenseCreated =expenseRepository.insert(expense);

        if (expenseCreated != null) {
            return Response.status(Response.Status.CREATED)
                    .entity("Expense created successfully")
                    .build();
        }
        else
        {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Failed to create expense")
                    .build();
        }
    }

    public Response getAllExpenses(String houseId)
    {
        return Response.ok(expenseRepository.findByHouseId(houseId)).build();
    }
}
