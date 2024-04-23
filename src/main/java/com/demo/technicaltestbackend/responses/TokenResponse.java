package com.demo.technicaltestbackend.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TokenResponse extends BasicResponse{
    private String accessToken;
}
