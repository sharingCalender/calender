package sharingcalender.calender.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequestDto(

    @NotBlank
    String name,

    @NotBlank
    String email,

    @NotBlank
    String mobile,

    @NotBlank
    String username,

    String password,

    String provider

){}
