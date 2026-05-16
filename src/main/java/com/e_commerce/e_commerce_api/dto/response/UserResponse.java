
package com.e_commerce.e_commerce_api.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserResponse extends BaseResponse {
    private Long id;
    private String email;
    private String userName;
    private String displayName;
}
