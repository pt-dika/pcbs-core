package dk.apps.pcps.dbmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dk.apps.pcps.dbmaster.entity.MerchantGroup;

public interface MerchantGroupRepository extends JpaRepository<MerchantGroup, Integer> {
}
