package ru.slisarenko.objectmapper.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.objectmapper.model.entity.Order;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.model.enums.OrderStatus;
import ru.slisarenko.objectmapper.model.repository.OrderRepository;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;
import ru.slisarenko.objectmapper.service.exception.NotFoundCustomer;
import ru.slisarenko.objectmapper.service.exception.NotFoundOrder;
import ru.slisarenko.objectmapper.service.exception.NotFoundProduct;
import ru.slisarenko.objectmapper.service.interfaces.CustomerService;
import ru.slisarenko.objectmapper.service.interfaces.OrderService;
import ru.slisarenko.objectmapper.service.interfaces.ProductService;
import ru.slisarenko.objectmapper.service.mapper.MapperJson;
import ru.slisarenko.objectmapper.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final OrderMapper mapper;
    private final ProductService productService;
    private final MapperJson jsonMapper;

    @Override
    public String createOrder(String jsonRequestDto) throws JsonProcessingException {

        var orderRequestDTO = this.jsonMapper.deserializeOrderRequestDto(jsonRequestDto);
        var customer = this.customerService.getCustomerToCreateOrder(orderRequestDTO.emailOrContactNumber())
                .orElseThrow(() -> new NotFoundCustomer(orderRequestDTO.emailOrContactNumber()));

        var products = orderRequestDTO.productIds().stream()
                .map(id -> this.productService.getProductToCreateOrder(id).orElseThrow(() -> new NotFoundProduct(id)))
                .toList();
        var totalPrice = products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        var newOrder = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(totalPrice)
                .shippingAddress(orderRequestDTO.shippingAddress())
                .products(products)
                .build();

        newOrder = this.orderRepository.save(newOrder);
        var orderResponseDTO = this.mapper.toDto(newOrder);
        return this.jsonMapper.serializeOrderResponseDto(orderResponseDTO);
    }

    @Override
    public String getOrder(Long orderId) throws JsonProcessingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundOrder(orderId));
        List<Long> productsDTO = order.getProducts().stream()
                .map(Product::getProductId)
                .toList();

        var responseOrder =  OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomer().getCustomerId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .shippingAddress(order.getShippingAddress())
                .productIds(productsDTO)
                .build();

        return this.jsonMapper.serializeOrderResponseDto(responseOrder);

    }
}
