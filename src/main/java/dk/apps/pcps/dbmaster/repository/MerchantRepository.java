package dk.apps.pcps.dbmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.dbmaster.entity.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

}
