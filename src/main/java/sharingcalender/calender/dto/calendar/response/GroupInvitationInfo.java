package sharingcalender.calender.dto.calendar.response;

public record GroupInvitationInfo (
    long calendarGroupId,
    String usernameFrom,
    String groupName,
    long groupInvitationId
){}
