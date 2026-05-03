package ru.slisarenko.jsonview.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ru.slisarenko.jsonview.controller.Views.CustomerSummary;

public record CustomerCreateDataDTO(@JsonView(CustomerSummary.class) String name,
                                    @JsonView(CustomerSummary.class) String email) {
}
