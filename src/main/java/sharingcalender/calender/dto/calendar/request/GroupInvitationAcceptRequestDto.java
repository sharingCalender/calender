package sharingcalender.calender.dto.calendar.request;

import jakarta.validation.constraints.Min;

public record GroupInvitationAcceptRequestDto (


    @Min(0L)
    long calendarGroupId,

    @Min(0L)
    long groupInvitationId

){}
