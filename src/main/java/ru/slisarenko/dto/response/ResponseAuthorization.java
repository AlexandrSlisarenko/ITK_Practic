package ru.slisarenko.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ResponseAuthorization {
    private String accessToken;
    private String refreshToken;
    private User user;
}
