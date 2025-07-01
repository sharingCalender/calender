package sharingcalender.calender.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sharingcalender.calender.adapter.MailAdapter;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailgunService implements MailService {

    private final MailAdapter mailAdapter;
//    private final Executor mailExecutor;


    public void sendEmailInGroup(long calendarGroupId,String from,String[] to,String title,String text,String recipientVariables){

        mailAdapter.sendEmail(from, to, title, text, recipientVariables);
        
//        List<CompletableFuture<Void>> mailingTask = userListInGroup.stream()
//            .map(user -> MailgunSendEmailFormDto.create(FROM, user.email(), title, text))
//            .map(
//                mail -> CompletableFuture.runAsync(() -> mailAdapter.sendEmail(mail), mailExecutor))
//            .collect(Collectors.toList());
//
//        CompletableFuture
//            .allOf(mailingTask.toArray(new CompletableFuture[0]))
//            .join();

        log.info("[MailService.sendEmailInGroup] groupId : {}, 메일 전송완료", calendarGroupId);
    }

}
