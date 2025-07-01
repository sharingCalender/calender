package sharingcalender.calender.service.mail;

public interface MailService {

    void sendEmailInGroup(long calendarGroupId,String from,String[] to,String title,String text,String recipientVariables);
}
