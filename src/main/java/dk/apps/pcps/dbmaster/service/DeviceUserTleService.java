package dk.apps.pcps.dbmaster.service;

import dk.apps.pcps.dbmaster.entity.DeviceUserTle;

public interface DeviceUserTleService {

    DeviceUserTle getUserTle(String tid, String mid);
}
