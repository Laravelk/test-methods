package org.nsu.fit.tests.api;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.testng.annotations.Test;

import javax.ws.rs.BadRequestException;

public class BadCustomerLastName implements TestInterface{
    @Test(description = "Create user with wrong last name - too short.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void shortNameTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = "Ы";
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong last name - not start with upper case .", expectedExceptions = { BadRequestException.class })
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameNotStartsWithUpperCaseTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = "pEtrov";
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong last name - contains digits.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameContainsDigitsTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = "Petrov123";
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong last name - contains symbols.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameContainsSymbolTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = "Petrov&";
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

    @Test(description = "Create user with wrong last name - contains space.")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Create customer feature.")
    public void nameContainsSpaceTest() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = new Faker().name().firstName();
        customer.lastName = "Petrov Petrov";
        customer.login = String.format("%s_%s@example.com", customer.firstName, customer.lastName);
        customer.pass = "strongpass";
        client.createUnValidCustomer(adminToken, customer);
    }

}
