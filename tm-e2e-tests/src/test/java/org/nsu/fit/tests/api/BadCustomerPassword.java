package org.nsu.fit.tests.api;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.testng.annotations.Test;

import javax.ws.rs.BadRequestException;

public class BadCustomerPassword implements TestInterface{
    @Test(description = "Create user with short password.", expectedExceptions = { BadRequestException.class })
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void shortPasswordTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = new Faker().name().lastName();;
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "s";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with simple password.", expectedExceptions = { BadRequestException.class })
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void simplePasswordTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = new Faker().name().lastName();;
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "123qwe";
        client.createUnValidCustomer(adminToken, customer);
    }
}
