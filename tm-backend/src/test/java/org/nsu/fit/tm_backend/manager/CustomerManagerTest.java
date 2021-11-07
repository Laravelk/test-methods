package org.nsu.fit.tm_backend.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Authority;
import org.slf4j.Logger;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    private CustomerPojo createCustomerInput;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);

        // Создаем класс, методы которого будем тестировать,
        // и передаем ему наши mock объекты.
        customerManager = new CustomerManager(dbService, logger);
    }

    @Test
    void testCreateCustomer() {
        // настраиваем mock.
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerOutput);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.createCustomer(createCustomerInput);

        // Проверяем результат выполенния метода
        assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).createCustomer(createCustomerInput);

        // Проверяем, что другие методы не вызывались...
//        verify(dbService, times(0)).getCustomers();

//        assertEquals(customer,dbService.getCustomerByLogin(customer.login));
    }


    @Test
    void testCreateCustomerWithNullArgument_Right() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(null));
        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithSimplePassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
    }
    @Test
    void testCreateCustomerWithNullPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = null;
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.pass' is null.", exception.getMessage());
    }
    @Test
    void testCreateCustomerWithLongPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "124sse1s4q11ghxf";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithTheSameLogin() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John3";
        createCustomerInput.lastName = "Wick2";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "Baba_Jaga4";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomer1 = new CustomerPojo();
        createCustomer1.id = UUID.randomUUID();
        createCustomer1.firstName = "John";
        createCustomer1.lastName = "Wick";
        createCustomer1.login = "john_wick@example.com";
        createCustomer1.pass = "Baba_Jaga";
        createCustomer1.balance = 0;

        when(dbService.getCustomerByLogin(createCustomerInput.login)).thenReturn(createCustomer1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Same email.", exception.getMessage());
    }

    @Test
    void testLookupCustomer() {
        List<CustomerPojo> customers = new ArrayList<>();

        when(dbService.getCustomers()).thenReturn(customers);

        assertNull(customerManager.lookupCustomer("john_wick@example.com"));

    }

    @Test
    void testMeCustomer() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        Set<String> roles = new HashSet<>();
        roles.add(Authority.CUSTOMER_ROLE);

        AuthenticatedUserDetails user = new AuthenticatedUserDetails(
                UUID.randomUUID().toString(),
                createCustomerInput.login, roles);

        when(dbService.getCustomerByLogin(user.getName())).thenReturn(createCustomerInput);
        assertEquals(createCustomerInput.firstName, customerManager.me(user).firstName);
    }

    @Test
    void testMeAdmin() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        Set<String> roles = new HashSet<>();
        roles.add(Authority.ADMIN_ROLE);

        AuthenticatedUserDetails user = new AuthenticatedUserDetails(
                UUID.randomUUID().toString(),
                createCustomerInput.login, roles);

        when(dbService.getCustomerByLogin(user.getName())).thenReturn(createCustomerInput);
        assertEquals("admin", customerManager.me(user).login);
    }

    @Test
    void testTopUpBalanceRightValue() {
        CustomerPojo createCustomer1 = new CustomerPojo();
        createCustomer1.id = UUID.randomUUID();
        createCustomer1.firstName = "John";
        createCustomer1.lastName = "Wick";
        createCustomer1.login = "john_wick@example.com";
        createCustomer1.pass = "Baba_Jaga";
        createCustomer1.balance = 0;

        TopUpBalancePojo updateWallet = new TopUpBalancePojo();
        updateWallet.customerId = createCustomer1.id;
        updateWallet.money = 1;
        ArgumentCaptor<CustomerPojo> customerCaptor = ArgumentCaptor.forClass(CustomerPojo.class);

        when(dbService.getCustomer(updateWallet.customerId)).thenReturn(createCustomer1);
        customerManager.topUpBalance(updateWallet);
        Mockito.verify(dbService).editCustomer(customerCaptor.capture());
        CustomerPojo changedCustomer = customerCaptor.getValue();

        assertEquals(changedCustomer.balance, createCustomer1.balance);
    }
    @Test
    void testTopUpBalanceWrongValue() {
        CustomerPojo createCustomer1 = new CustomerPojo();
        createCustomer1.id = UUID.randomUUID();

        TopUpBalancePojo updateWallet = new TopUpBalancePojo();
        updateWallet.customerId = createCustomer1.id;
        updateWallet.money = -1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.topUpBalance(updateWallet));
        assertEquals("Top downed", exception.getMessage());
    }

    @Test
    void getCustomers() {
        List<CustomerPojo> customers = new ArrayList<>();

        CustomerPojo createCustomer1 = new CustomerPojo();
        createCustomer1.id = UUID.randomUUID();
        createCustomer1.firstName = "John";
        createCustomer1.lastName = "Wick";
        createCustomer1.login = "john_wick@example.com";
        createCustomer1.pass = "Baba_Jaga";
        createCustomer1.balance = 0;

        customers.add(createCustomer1);

        when(dbService.getCustomers()).thenReturn(customers);

        assertEquals(customers,customerManager.getCustomers());
    }

    @Test
    void testGetCustomerNotInDB() {
        UUID id = UUID.randomUUID();
        when(dbService.getCustomer(id)).thenThrow(new IllegalArgumentException("Customer with id '" + id + " was not found."));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.getCustomer(id));
        assertEquals("Customer with id '" + id + " was not found.", exception.getMessage());
    }

    @Test
    void testDeleteCustomer() {
        UUID id = UUID.randomUUID();
        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);

        customerManager.deleteCustomer(id);

        Mockito.verify(dbService).deleteCustomer(idCaptor.capture());
        UUID idCaptured = idCaptor.getValue();
        assertEquals(id, idCaptured);
    }
}