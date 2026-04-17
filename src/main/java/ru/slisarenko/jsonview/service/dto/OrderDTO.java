package ru.slisarenko.jsonview.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.math.BigDecimal;
import lombok.Builder;
import ru.slisarenko.jsonview.controller.Views.CustomerDetails;
import ru.slisarenko.jsonview.model.enums.StatusOrder;

@Builder
public record OrderDTO(@JsonView(CustomerDetails.class) StatusOrder status,
                       @JsonView(CustomerDetails.class) BigDecimal totalPrice) {
}
