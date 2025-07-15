package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventModifyRequestDto (
    @Min(0L)
    long eventId,
    @NotBlank
    String title,
    @NotBlank
    LocalDateTime start,
    @NotBlank
    LocalDateTime end,
    @NotNull
    String description,
    @Min(0L)
    long calendarGroupId
){}
