package com.coinquyteam.expense.Service;

import com.coinquyteam.expense.Data.CategoryExpense;
import com.coinquyteam.expense.Data.Debt;
import com.coinquyteam.expense.Data.Expense;
import com.coinquyteam.expense.Data.StatusExpense;
import com.coinquyteam.expense.Repository.IExpenseRepository;
import com.coinquyteam.expense.Utility.ExpenseDebtResult;
import com.coinquyteam.expense.Utility.ExpenseResult;
import com.coinquyteam.expense.Utility.ExpenseStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.coinquyteam.expense.Data.StatusExpense.PENDING;
import static com.coinquyteam.expense.Data.StatusExpense.SETTLED;

@Service
public class ExpenseService {

    private final IExpenseRepository expenseRepository;

    public ExpenseService(IExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseResult createExpense(String expenseDescription, Date expenseDate, Double expenseAmount,
                                       String houseId, String createdBy, CategoryExpense category, List<String> participants)
    {
        if(participants.isEmpty()){
            return new ExpenseResult(ExpenseStatus.INVALID_INPUT, "At least two participants are required for an expense", null);
        }

        Expense expense = new Expense(expenseDescription, expenseAmount, createdBy, expenseDate, category, houseId, participants);

        try
        {
            expenseRepository.insert(expense);
            return new ExpenseResult(ExpenseStatus.SUCCESS, "Expense created successfully", expense);
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

    public ExpenseDebtResult calculateDebt(String houseId) {

        List<Expense> expenses = expenseRepository.findByHouseId(houseId);

        if (expenses == null || expenses.isEmpty()) {
            return new ExpenseDebtResult(ExpenseStatus.NO_CONTENT, "No expenses found for the house", null);
        }

        List<Debt> debts = new ArrayList<>();

        for (Expense expense : expenses) {
            // Creo una mappa partecipante->debito
            if (expense.getParticipants().size() > 1 && expense.getStatus().equals(PENDING)) {

                Double amount = expense.getAmount()/expense.getParticipants().size();

                //Creo il debito
                Debt debt = new Debt();
                debt.setCreatedBy(expense.getCreatedBy());
                Map<String, Double> debitors = new HashMap<>();

                for (String partecipante : expense.getParticipants()) {

                    if(!partecipante.equals(expense.getCreatedBy())) {
                        // Se il partecipante non è colui che ha creato la spesa deve essere incluso nei debiti
                        debitors.put(partecipante, amount);
                    }
                }

                debt.setParticipants(debitors);

                // Aggiungo il debito alla lista dei debiti
                debts.add(debt);
            }
        }

        return new ExpenseDebtResult(ExpenseStatus.SUCCESS, "Debts calculated successfully", debts);
    }


    public ExpenseResult updateExpenseStatus(String expenseId) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            if (expense.getStatus() == PENDING) {
                expense.setStatus(SETTLED);
                expenseRepository.save(expense);
                return new ExpenseResult(ExpenseStatus.SUCCESS, "Expense status updated to SETTLED", expense);
            } else {
                return new ExpenseResult(ExpenseStatus.ERROR, "Expense is already SETTLED", null);
            }
        } else {
            return new ExpenseResult(ExpenseStatus.ERROR, "Expense not found", null);
        }
    }

}
