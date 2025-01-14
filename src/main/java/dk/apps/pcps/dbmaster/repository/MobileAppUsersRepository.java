package dk.apps.pcps.dbmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;

import java.util.Optional;

@Repository
public interface MobileAppUsersRepository extends JpaRepository<MobileAppUsers, Integer> {

    Optional<MobileAppUsers> findByUsername(String username);
}
