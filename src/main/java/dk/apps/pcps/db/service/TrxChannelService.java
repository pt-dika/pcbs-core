package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.TrxChannel;

public interface TrxChannelService {
    TrxChannel getTrxChannel(int batchGroup);
    TrxChannel getTrxChannel(boolean isFtp, int batchGroupId);
}
