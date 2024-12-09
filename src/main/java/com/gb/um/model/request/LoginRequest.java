package com.gb.um.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotNull(message = "email cannot be empty/null.")
    @Email(message = "Invalid Email")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "password cannot be empty/null.")
    @JsonProperty("password")
    private String password;
}
