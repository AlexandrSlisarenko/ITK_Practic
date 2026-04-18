package ru.slisarenko.objectmapper.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.slisarenko.objectmapper.model.entity.Order;
import ru.slisarenko.objectmapper.model.entity.Product;
import ru.slisarenko.objectmapper.model.enums.OrderStatus;
import ru.slisarenko.objectmapper.model.repository.OrderRepository;
import ru.slisarenko.objectmapper.service.dto.OrderRequestDTO;
import ru.slisarenko.objectmapper.service.dto.OrderResponseDTO;
import ru.slisarenko.objectmapper.service.dto.ProductDTO;
import ru.slisarenko.objectmapper.service.exception.NotFoundCustomer;
import ru.slisarenko.objectmapper.service.exception.NotFoundOrder;
import ru.slisarenko.objectmapper.service.exception.NotFoundProduct;
import ru.slisarenko.objectmapper.service.interfaces.CustomerService;
import ru.slisarenko.objectmapper.service.interfaces.OrderService;
import ru.slisarenko.objectmapper.service.interfaces.ProductService;
import ru.slisarenko.objectmapper.service.mapper.OrderMapper;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final OrderMapper mapper;
    private final ProductService productService;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {


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

        return this.mapper.toDto(newOrder);
    }

    @Override
    public OrderResponseDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundOrder(orderId));
        List<ProductDTO> productsDTO = order.getProducts().stream()
                .map(productService::mappingProductToDTO)
                .toList();

        return OrderResponseDTO.builder()
                .customer(order.getCustomer())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .shippingAddress(order.getShippingAddress())
                .products(productsDTO)
                .build();

    }
}
