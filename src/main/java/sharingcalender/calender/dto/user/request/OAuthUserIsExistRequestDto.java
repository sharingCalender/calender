package sharingcalender.calender.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record OAuthUserIsExistRequestDto(
    @NotBlank
    String id,
    @NotBlank
    String name,
    @NotBlank
    String mobile,
    @NotBlank
    String email,

    String provider,

    String password
){}
