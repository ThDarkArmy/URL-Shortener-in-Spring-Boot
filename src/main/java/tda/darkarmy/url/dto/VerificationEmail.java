package tda.darkarmy.url.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class VerificationEmail {
    private String subject;
    private String recipient;
    private String body;
    private String verificationLink;
}
