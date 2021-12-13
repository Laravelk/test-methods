package org.nsu.fit.tests.api;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.testng.annotations.Test;

import javax.ws.rs.BadRequestException;

public class BadPlanFee implements TestInterface{
    @Test(description = "Create plan with negative fee.", expectedExceptions = { BadRequestException.class })
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create plan feature.")
    public void feeIsNegativeTest(){
        PlanPojo plan = new PlanPojo();
        plan.name = new Faker().name().name();
        plan.details = "Revenge for the dog";
        plan.fee = -1;
        client.createUnValidPlan(adminToken,plan);
    }


    @Test(description = "Create plan with too high fee.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create plan feature.")
    public void feeIsHighTest(){
        PlanPojo plan = new PlanPojo();
        plan.name = new Faker().name().name();
        plan.details = "Revenge for the dog";
        plan.fee = 4444;
        client.createUnValidPlan(adminToken, plan);
    }
}
