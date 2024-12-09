package com.gb.um.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gb.um.repo.entity.Role;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @Nullable
    @JsonProperty("firstName")
    private String firstName;

    @Nullable
    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "email cannot be empty/null.")
    @Email(message = "Invalid Email")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "password cannot be empty/null.")
    @JsonProperty("password")
    private String password;

    @Nullable
    @JsonProperty("role")
    private Role role;

}
