package ru.slisarenko.jsonview.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import lombok.Builder;
import ru.slisarenko.jsonview.controller.Views.CustomerDetails;

@Builder
public record OrderRequestDTO(@JsonView(CustomerDetails.class) Long customerId,
                              @JsonView(CustomerDetails.class) List<ProductDTO> products ) {
}
