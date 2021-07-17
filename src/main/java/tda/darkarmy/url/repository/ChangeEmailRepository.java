package tda.darkarmy.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.url.model.ChangeEmail;

public interface ChangeEmailRepository extends JpaRepository<ChangeEmail, Long> {
    ChangeEmail findByToken(String token);
}
