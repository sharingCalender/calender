package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;

public record CalendarGroupDeleteRequestDto (

    @Min(0L)
    long calendarGroupId
){}
