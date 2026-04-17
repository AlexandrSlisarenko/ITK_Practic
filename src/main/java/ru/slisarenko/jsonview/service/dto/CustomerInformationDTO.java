package ru.slisarenko.jsonview.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import lombok.Builder;
import ru.slisarenko.jsonview.controller.Views.CustomerDetails;
import ru.slisarenko.jsonview.controller.Views.CustomerSummary;

@Builder
public record CustomerInformationDTO(@JsonView(CustomerSummary.class) Long id,
                                     @JsonView(CustomerSummary.class) String name,
                                     @JsonView(CustomerSummary.class) String email,
                                     @JsonView(CustomerDetails.class) List<OrderDTO> orders) {
}
