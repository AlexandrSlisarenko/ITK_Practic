package ru.slisarenko.persist.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slisarenko.entity_library.dto.ShopOrderInformationStatusDTO;
import ru.slisarenko.entity_library.dto.order.OrderRequestDTO;
import ru.slisarenko.entity_library.dto.payment.PaymentRequestDTO;
import ru.slisarenko.entity_library.dto.persist.PersistDTO;
import ru.slisarenko.entity_library.dto.shipping.ShippingRequestDTO;
import ru.slisarenko.entity_library.enums.OrderStatus;
import ru.slisarenko.persist.entity.ShopOrder;
import ru.slisarenko.persist.entity.ShopProduct;
import ru.slisarenko.persist.mapping.OrderMapping;
import ru.slisarenko.persist.repository.ShopCustomerRepository;
import ru.slisarenko.persist.repository.ShopOrderRepository;
import ru.slisarenko.persist.repository.ShopProductRepository;

@Service
@Transactional
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
        if (!checkCustomerExists(requestDTO.customerId())) {
            return getErrorResponse();
        }
        var products = new ArrayList<ShopProduct>();
        for (Long id : requestDTO.productIds()) {
            if (!checkProductExists(id)) {
                return getErrorResponse();
            }
            products.add(this.shopProductRepository.getReferenceById(id));
        }

        var order = ShopOrder.builder()
                .status(OrderStatus.CREATED)
                .customer(this.shopCustomerRepository.getReferenceById(requestDTO.customerId()))
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

    private boolean checkProductExists(Long productId) {
        return this.shopProductRepository.existsById(productId);
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

}
