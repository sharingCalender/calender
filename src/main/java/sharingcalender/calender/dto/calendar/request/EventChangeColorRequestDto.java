package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EventChangeColorRequestDto (
    @Min(0L)
    long eventId,
    @NotBlank
    String backgroundColor,
    @NotBlank
    String borderColor
){}
