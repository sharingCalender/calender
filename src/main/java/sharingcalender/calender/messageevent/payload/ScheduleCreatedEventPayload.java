package sharingcalender.calender.messageevent.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sharingcalender.calender.messageevent.MessageEventPayload;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleCreatedEventPayload implements MessageEventPayload {
    private long groupId;
    private String groupName;
    private String scheduleTitle;

    private ScheduleCreatedEventPayload(long groupId,String groupName, String scheduleTitle) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.scheduleTitle = scheduleTitle;
    }

    public static ScheduleCreatedEventPayload create(long groupId,String groupName, String scheduleTitle){
        return new ScheduleCreatedEventPayload(groupId,groupName, scheduleTitle);
    }
}
