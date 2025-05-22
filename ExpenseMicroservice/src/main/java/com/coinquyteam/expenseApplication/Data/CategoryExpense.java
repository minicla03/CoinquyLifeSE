
package com.coinquyteam.expenseApplication.Data;

public enum CategoryExpense
{
    FOOD("Cibo"),
    TRANSPORT("Trasporti"),
    ENTERTAINMENT("Intrattenimento"),
    SHOPPING("Shopping"),
    HEALTH("Salute"),
    BILLS("Bollette"),
    OTHER("Altro");

    private final String name;

    CategoryExpense(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
