package sharingcalender.calender.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import sharingcalender.calender.config.FeignClientConfig;
import sharingcalender.calender.dto.email.response.MailgunSendingResponseDto;

@FeignClient(name = "mail", url = "${mailgun.url}",configuration = FeignClientConfig.class)
public interface MailAdapter {

    @PostMapping(
        value = "/sandbox91570acdf52b4793bb55ccebe346386b.mailgun.org/messages",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<MailgunSendingResponseDto> sendEmail(
        @RequestPart("from") String from,
        @RequestPart("to") String[] to,
        @RequestPart("subject") String subject,
        @RequestPart("text") String text,
        @RequestPart("recipient-variables") String recipientVariables);
}
