package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;

public record EventDeleteRequestDto (
    @Min(0L)
    long eventId
){}
