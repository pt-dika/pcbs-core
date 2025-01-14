package dk.apps.pcps.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.db.entity.Cryptography;

@Repository
public interface CryptographyRepository extends JpaRepository<Cryptography, Integer> {

}
