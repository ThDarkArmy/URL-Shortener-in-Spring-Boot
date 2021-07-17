package tda.darkarmy.url.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ResetPasswordRequest {
    private String email;
    private String password;
}
