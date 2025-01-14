package dk.apps.pcps.dbmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.dbmaster.entity.MobileAppUserSession;

import java.util.Optional;

@Repository
public interface MobileAppUserSessionRepository extends JpaRepository<MobileAppUserSession, Integer> {

    Boolean existsByUsernameAndToken(String username, String token);
    Boolean existsByUsernameAndTokenAndHitFrom(String username, String token, String hitFrom);
    MobileAppUserSession findByUsername(String username);
    Optional<MobileAppUserSession> findByUsernameAndHitFrom(String username, String hitFrom);
}
