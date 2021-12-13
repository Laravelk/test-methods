package org.nsu.fit.tests.api;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.testng.annotations.Test;

import javax.ws.rs.BadRequestException;

public class BadCustomerFirstName implements TestInterface {
    @Test(description = "Create user with wrong first name.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void shortNameTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "Ы";
        customer.lastName = new Faker().name().lastName();
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong first name.", expectedExceptions = { BadRequestException.class })
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameNotStartsWithUpperCaseTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "pEtr";
        customer.lastName = new Faker().name().lastName();
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong first name.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameContainsSymbolTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "Petr&";
        customer.lastName = new Faker().name().lastName();
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong first name.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameContainsSpaceTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "Petr Petrovich";
        customer.lastName = new Faker().name().lastName();
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

}
