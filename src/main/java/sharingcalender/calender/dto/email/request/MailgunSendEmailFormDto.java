package sharingcalender.calender.dto.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MailgunSendEmailFormDto (
    String from,
    String[] to,
    String subject,
    String text,
    @JsonProperty("recipient-variables")
    String recipientVariables
){

    public static MailgunSendEmailFormDto create(String from, String[] to, String subject,String text,String recipientVariables){
        return new MailgunSendEmailFormDto(from, to, subject, text, recipientVariables);
    }
}
