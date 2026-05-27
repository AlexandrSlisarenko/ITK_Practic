package ru.slisarenko.persist.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.entity_library.dto.shipping.ShippingRequestDTO;
import ru.slisarenko.entity_library.enums.OrderStatus;
import ru.slisarenko.persist.entity.ShopCustomer;
import ru.slisarenko.persist.entity.ShopOrder;
import ru.slisarenko.persist.entity.ShopProduct;
import ru.slisarenko.persist.mapping.OrderMapping;
import ru.slisarenko.persist.repository.ShopCustomerRepository;
import ru.slisarenko.persist.repository.ShopOrderRepository;
import ru.slisarenko.persist.repository.ShopProductRepository;

@Service
@Transactional("transactionManager")
@RequiredArgsConstructor
public class ShopService {
    private final ShopProductRepository shopProductRepository;
    private final ShopCustomerRepository shopCustomerRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final OrderMapping orderMapping;

    public PersistDTO payOrder(PaymentRequestDTO requestDTO) {
        if (!checkCustomerExists(requestDTO.customerId())) {
            return getErrorResponse();
        }

        if (!checkOrderExists(requestDTO.customerId())) {
            return getErrorResponse();
        }

        var price = getPriceOfOrder(requestDTO.orderId());
        var cash = getCash(requestDTO.orderId());

        if (cash.compareTo(price) < 0) {
            return getErrorResponse();
        }
        var order = updateStatus(requestDTO.orderId(), OrderStatus.PAYMENT);

        return PersistDTO.builder()
                .requestUUId(requestDTO.requestUUId())
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .build();
    }

    public PersistDTO deliveryOrder(ShippingRequestDTO requestDTO) {
        if (!checkCustomerExists(requestDTO.customerId())) {

            return getErrorResponse();
        }

        if (!checkOrderExists(requestDTO.customerId())) {
            return getErrorResponse();
        }

        var order = updateStatus(requestDTO.orderId(), OrderStatus.SHIPPING);

        return PersistDTO.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .build();
    }

    public PersistDTO createOrder(PersistDTO requestDTO) {
        var customer = createCustomer(requestDTO.customerId());
        var products = new ArrayList<ShopProduct>();
        for (Long id : requestDTO.productIds()) {
            products.add(getProduct(id));
        }

        var order = ShopOrder.builder()
                .status(OrderStatus.CREATED)
                .customer(customer)
                .products(products)
                .orderUUID(requestDTO.requestUUId())
                .build();
        order = saveOrder(order);
        return this.orderMapping.toPersistDTO(order);
    }

    private BigDecimal getPriceOfOrder(Long orderId) {
        var order = this.shopOrderRepository.findById(orderId).orElseGet(ShopOrder::new);
        return order.getProducts().stream()
                .map(ShopProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getCash(Long customerId) {
        return this.shopCustomerRepository.getReferenceById(customerId).getCash();
    }

    private boolean checkCustomerExists(Long customerId) {
        return this.shopCustomerRepository.existsById(customerId);
    }

    private ShopProduct getProduct(Long productId) {
        var product = this.shopProductRepository.findById(productId)
                .orElseGet(() -> ShopProduct.builder()
                        .price(BigDecimal.valueOf(10L))
                        .build());
        return this.shopProductRepository.save(product);
    }

    private boolean checkOrderExists(Long orderId) {
        return this.shopOrderRepository.existsById(orderId);
    }

    private PersistDTO getErrorResponse() {
        return PersistDTO.builder()
                .orderId(-1L)
                .status(OrderStatus.ERROR)
                .build();
    }

    private ShopOrder saveOrder(ShopOrder order) {
        return this.shopOrderRepository.save(order);
    }

    private ShopOrder updateStatus(Long orderId, OrderStatus orderStatus) {
        var order = shopOrderRepository.findById(orderId).orElseGet(ShopOrder::new);
        order.setStatus(OrderStatus.PAYMENT);
        return saveOrder(order);
    }

    private ShopCustomer createCustomer(Long customerId) {
       var customer = this.shopCustomerRepository.findById(customerId)
                .orElseGet(() -> ShopCustomer.builder()
                        .cash(BigDecimal.valueOf(1000000L))
                        .build());
        return this.shopCustomerRepository.save(customer);

    }

}
