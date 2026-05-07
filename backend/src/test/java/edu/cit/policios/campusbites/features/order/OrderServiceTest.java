package edu.cit.policios.campusbites.features.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_SetsDefaultStatus() {
        Order order = new Order();
        order.setUserId("user1");
        order.setTotalAmount(25.0);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrder(order);

        assertEquals("pending", result.getStatus());
        assertNotNull(result.getOrderDate());
    }

    @Test
    void createOrder_SavesAndReturns() {
        Order order = new Order("user1", null, 30.0, "Campus St 1");
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        verify(orderRepository).save(order);
    }

    @Test
    void getOrdersByUserId_ReturnsUserOrders() {
        Order o1 = new Order("user1", null, 20.0, "Addr 1");
        Order o2 = new Order("user1", null, 35.0, "Addr 2");
        when(orderRepository.findByUserId("user1")).thenReturn(Arrays.asList(o1, o2));

        List<Order> result = orderService.getOrdersByUserId("user1");

        assertEquals(2, result.size());
    }

    @Test
    void getOrderById_Exists_ReturnsOrder() {
        Order order = new Order("user1", null, 15.0, "Street 1");
        order.setId("ord123");
        when(orderRepository.findById("ord123")).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById("ord123");

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
    }

    @Test
    void getOrderById_NotFound_ReturnsNull() {
        when(orderRepository.findById("missing")).thenReturn(Optional.empty());

        Order result = orderService.getOrderById("missing");

        assertNull(result);
    }

    @Test
    void updateOrderStatus_UpdatesSuccessfully() {
        Order order = new Order("user1", null, 20.0, "Addr");
        order.setId("ord1");
        order.setStatus("pending");
        when(orderRepository.findById("ord1")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.updateOrderStatus("ord1", "confirmed");

        assertNotNull(result);
        assertEquals("confirmed", result.getStatus());
    }
}