package tda.darkarmy.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.url.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
