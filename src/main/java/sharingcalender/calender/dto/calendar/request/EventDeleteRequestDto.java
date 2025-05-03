package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EventDeleteRequestDto (
    @Min(0L)
    long eventId,
    @Min(0L)
    long calendarGroupId,
    @NotBlank
    String firstDateOfMonth
){}
