package dk.apps.pcps.dbmaster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.dbmaster.entity.DeviceUserTle;

import java.util.Optional;

@Repository
public interface DeviceUserTleRepository extends JpaRepository<DeviceUserTle, Integer> {
    Optional<DeviceUserTle> findByTidAndMid(String tid, String mid);
}
