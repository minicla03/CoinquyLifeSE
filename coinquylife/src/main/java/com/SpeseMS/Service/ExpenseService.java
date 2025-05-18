package com.SpeseMS.Service;

import com.SpeseMS.Data.CategoryExpense;
import com.SpeseMS.Data.Expense;
import com.SpeseMS.Repository.IExpenseRepository;
import com.SpeseMS.Utility.ExpenseResult;
import com.SpeseMS.Utility.ExpenseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired private IExpenseRepository expenseRepository;

    public ExpenseResult createExpense(String expenseDescription, Date expenseDate, Double expenseAmount,
                                       String houseId, String createdBy, CategoryExpense category, List<String> participants)
    {
        Expense expense = new Expense(expenseDescription, expenseAmount, createdBy, expenseDate, category, houseId, participants);

        try
        {
            expenseRepository.insert(expense);
            return new ExpenseResult(ExpenseStatus.SUCCESS, "Expense created successfully", null);
        }
        catch (Exception e)
        {
            return new ExpenseResult(ExpenseStatus.ERROR, "Error creating expense: " + e.getMessage(), null);
        }
    }

    public ExpenseResult getAllExpenses(String houseId)
    {
        List<Expense> expenses= expenseRepository.findByHouseId(houseId);
        if(expenses != null && !expenses.isEmpty())
        {
            return new ExpenseResult(ExpenseStatus.SUCCESS, "Expenses retrieved successfully", expenses);
        }
        else
        {
            return new ExpenseResult(ExpenseStatus.NO_CONTENT, "No expenses found", null);
        }
    }
}
