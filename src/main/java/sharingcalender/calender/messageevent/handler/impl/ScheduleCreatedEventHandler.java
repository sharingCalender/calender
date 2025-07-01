package sharingcalender.calender.messageevent.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sharingcalender.calender.dto.user.response.UserEmailSendingInfoResponseDto;
import sharingcalender.calender.messageevent.MessageEvent;
import sharingcalender.calender.messageevent.MessageEventType;
import sharingcalender.calender.messageevent.handler.MessageEventHandler;
import sharingcalender.calender.messageevent.payload.ScheduleCreatedEventPayload;
import sharingcalender.calender.service.mail.MailService;
import sharingcalender.calender.service.calendar.UserGroupService;
import sharingcalender.calender.util.serializer.DataSerializer;

@Component
@RequiredArgsConstructor
public class ScheduleCreatedEventHandler implements MessageEventHandler<ScheduleCreatedEventPayload> {

    private final UserGroupService userGroupService;
    private final MailService mailService;
    private static final String TITLE = "%s 에 새로운 일정이 등록되었습니다.";
    private static final String FROM = "Mailgun Sandbox <postmaster@sandbox91570acdf52b4793bb55ccebe346386b.mailgun.org>";
    private static final String TO = "%s <%s>";
    @Override
    public void handle(MessageEvent<ScheduleCreatedEventPayload> messageEvent) {

        ScheduleCreatedEventPayload payload = messageEvent.getPayload();

        Map<String, Map<String, String>> variables = new HashMap<>();

        String[] to = getToArray(payload,variables);
        String recipientVariables = DataSerializer.serialize(variables);
        String title = String.format(TITLE, payload.getGroupName());
        String text = getText(payload.getGroupName(), payload.getScheduleTitle());

        mailService.sendEmailInGroup(payload.getGroupId(),FROM, to, title, text, recipientVariables);
    }

    private String[] getToArray(ScheduleCreatedEventPayload payload,Map<String, Map<String, String>> variables) {

        List<UserEmailSendingInfoResponseDto> userListInGroup = userGroupService.getUserListInGroup(
            payload.getGroupId());

        String[] to = new String[userListInGroup.size()];

        for(int i = 0; i < userListInGroup.size(); i++){
            UserEmailSendingInfoResponseDto user = userListInGroup.get(i);
            to[i] = String.format(TO, user.name(), user.email());
            variables.put(user.email(), Map.of("name", user.name()));
        }

        return to;
    }

    private String getText(String groupName,String scheduleTitle){
        return "%recipient.name% 님 \n" + groupName + " 그룹에 title: <" + scheduleTitle + "> 일정이 등록되었습니다.";
    }

    @Override
    public boolean isSupports(MessageEvent<ScheduleCreatedEventPayload> messageEvent) {
        return MessageEventType.SCHEDULE_CREATED == messageEvent.getType();
    }


}
