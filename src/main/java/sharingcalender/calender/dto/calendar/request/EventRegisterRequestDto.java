package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventRegisterRequestDto (

    @Min(0L)
    long calendarId,
    @NotBlank
    String title,
    @NotNull
    LocalDateTime start,
    @NotNull
    LocalDateTime end,
    @NotBlank
    String backgroundColor,
    @NotBlank
    String borderColor,
    @NotNull
    String description,
    @NotBlank
    String writer,
    @Min(0L)
    long calendarGroupId,
    @NotBlank
    String groupName
){}
