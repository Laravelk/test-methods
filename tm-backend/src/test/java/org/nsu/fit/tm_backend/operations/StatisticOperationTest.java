package org.nsu.fit.tm_backend.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class StatisticOperationTest {
    // Лабораторная 2: покрыть юнит тестами класс StatisticOperation на 100%.
    private CustomerManager customerManager;
    private SubscriptionManager subscriptionManager;
    private List<UUID> customerIds;

    private StatisticOperation statisticOperation;

    @BeforeEach
    void init() {
        customerManager = mock(CustomerManager.class);
        subscriptionManager = mock(SubscriptionManager.class);

        customerIds = Collections.emptyList();

        statisticOperation = new StatisticOperation(customerManager, subscriptionManager, customerIds);
    }

    @Test
    void testExecuteOperation() {
        StatisticOperation.StatisticOperationResult result = statisticOperation.Execute();

        assertEquals(result.overallBalance, 0);
        assertEquals(result.overallFee, 0);
    }

    @Test
    void testInitOperation1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(null, subscriptionManager, customerIds));
        assertEquals("customerManager", exception.getMessage());
    }

    @Test
    void testInitOperation2() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, null, customerIds));
        assertEquals("subscriptionManager", exception.getMessage());
    }

    @Test
    void testInitOpeartion3() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new StatisticOperation(customerManager, subscriptionManager, null));
        assertEquals("customerIds", exception.getMessage());
    }
}
