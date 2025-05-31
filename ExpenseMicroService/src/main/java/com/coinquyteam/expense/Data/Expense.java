package com.coinquyteam.expense.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;

    @Field("description")
    private String description;

    @Field("amount")
    private Double amount;

    @Field("created_by")
    private String createdBy; // User ID of who created the expense

    @Field("created_date")
    private Date createdDate;

    @Field("category")
    private CategoryExpense category;

    @Field("house_id")
    private String houseId;

    @Field("participants")
    private List<String> participants; // List of user IDs participating in this expens

    @Field("status")
    private StatusExpense status;

    public Expense() {
        this.createdDate = new Date();
    }

    public Expense(String description, Double amount, String createdBy, Date createdDate, CategoryExpense category, String houseId, List<String> participants)
    {
        this.description = description;
        this.amount = amount;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.category = category;
        this.houseId = houseId;
        this.participants = participants;
        this.status=StatusExpense.PENDING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public CategoryExpense getCategory() {
        return category;
    }

    public void setCategory(CategoryExpense category) {
        this.category = category;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public StatusExpense getStatus() {
        return status;
    }

    public void setStatus(StatusExpense status) {
        this.status = status;
    }
}
