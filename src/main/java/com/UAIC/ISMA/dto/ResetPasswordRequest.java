package com.UAIC.ISMA.dto;
import com.UAIC.ISMA.entity.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token;
    @NotBlank
    private String newPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String facultate;

    private String grupa;

    private String an;

    @NotNull
    private RoleName role;

}