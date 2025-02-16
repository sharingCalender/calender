package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GroupInvitationSaveRequestDto (
    @NotBlank
    String username,

    @Min(0L)
    long calendarGroupId
){}
