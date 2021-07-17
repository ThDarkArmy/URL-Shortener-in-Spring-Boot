package tda.darkarmy.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.url.dto.ResetPasswordRequest;
import tda.darkarmy.url.model.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    ResetPasswordRequest findByEmail(String email);

    ResetPassword findByVerificationToken(String token);
}
