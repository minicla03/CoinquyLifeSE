package com.coinquyteam.expense.Service;

import com.coinquyteam.expense.Data.CategoryExpense;
import com.coinquyteam.expense.Data.Expense;
import com.coinquyteam.expense.Repository.IExpenseRepository;
import com.coinquyteam.expense.Utility.ExpenseResult;
import com.coinquyteam.expense.Utility.ExpenseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    private final IExpenseRepository expenseRepository;

    public ExpenseService(IExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

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
