package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;

public record CalendarGroupDeleteRequestDto (

    @Min(1L)
    long calendarGroupId
){}
