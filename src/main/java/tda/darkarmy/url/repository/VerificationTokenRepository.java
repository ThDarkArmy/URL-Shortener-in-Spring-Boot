package tda.darkarmy.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.url.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
