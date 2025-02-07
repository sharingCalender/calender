package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.NotBlank;

public record CalendarGroupRegisterRequestDto(
    @NotBlank
    String groupName

){}
