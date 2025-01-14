package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.DeviceUserDetail;

public interface DeviceUserDetailService {

    int getSessionNumber(String username);
    DeviceUserDetail updateSessionNumber(String username, int sesNum);
    DeviceUserDetail getUserDetail(String username);
    DeviceUserDetail updateInvoiceNumber(String username, int invoiceNumber);
    DeviceUserDetail saveMarriageCode(String username, String marriageCode);

}
