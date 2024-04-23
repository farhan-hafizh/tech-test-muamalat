package com.demo.technicaltestbackend.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BasicResponse extends BaseResponse{
    private String message;
}
