package com.e_commerce.e_commerce_api.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SizeResponse extends BaseResponse {
    private Long id;
    private String name;
    private String description;
}
