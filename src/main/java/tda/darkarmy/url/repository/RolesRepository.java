package tda.darkarmy.url.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tda.darkarmy.url.model.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
}
