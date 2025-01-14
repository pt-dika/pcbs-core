package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.DeviceUserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface DeviceUserDetailRepository extends JpaRepository<DeviceUserDetail, Integer> {

    @Query(value = "SELECT max(invoiceNum) FROM DeviceUserDetail where username = ?1")
    Integer maxId(String username);
    Optional<DeviceUserDetail> findByUsername(String username);

}
