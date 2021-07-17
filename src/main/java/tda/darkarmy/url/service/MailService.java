package tda.darkarmy.url.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tda.darkarmy.url.dto.VerificationEmail;
import tda.darkarmy.url.exception.DarkException;
import tda.darkarmy.url.utils.MailContentsBuilder;


@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailContentsBuilder mailContentBuilder;

    @Async
    public void sendMail(VerificationEmail verificationEmail) throws DarkException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("exoccured@gmail.com");
            messageHelper.setTo(verificationEmail.getRecipient());
            messageHelper.setSubject(verificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(verificationEmail.getBody(), verificationEmail.getVerificationLink()), true);
        };

        try{
            javaMailSender.send(messagePreparator);
            log.info("Mail Sent!");
        }catch (MailException e){
            throw new DarkException("Exception occurred when sending mail to "+verificationEmail.getRecipient()+" : "+e.getMessage());
        }
    }
}