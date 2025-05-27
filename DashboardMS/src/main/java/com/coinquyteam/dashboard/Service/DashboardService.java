package com.coinquyteam.dashboard.Service;

import com.coinquyteam.dashboard.Config.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService
{
    @Autowired
    private RestTemplateConfig restTemplate;

    public void fetchDashboardData()
    {
        // Logic to fetch data from other microservices using restTemplate
        // This could involve making REST calls to the Expense, House, and User services
        // and aggregating the results for the dashboard.

        // Example:
        // String expenseData = restTemplate.getForObject("http://expense-service/expenses", String.class);
        // String houseData = restTemplate.getForObject("http://house-service/houses", String.class);
        // String userData = restTemplate.getForObject("http://user-service/users", String.class);

        // Process and combine the data as needed for the dashboard.
    }

}
