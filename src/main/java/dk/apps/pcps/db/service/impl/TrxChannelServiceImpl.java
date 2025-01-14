package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import dk.apps.pcps.db.entity.TrxChannel;
import dk.apps.pcps.db.repository.TrxChannelRepository;
import dk.apps.pcps.db.service.TrxChannelService;

import java.util.List;

@Service
public class TrxChannelServiceImpl implements TrxChannelService {

    TrxChannelRepository trxChannelRepository;

    @Autowired
    public TrxChannelServiceImpl(TrxChannelRepository trxChannelRepository){
        this.trxChannelRepository = trxChannelRepository;
    }

    @Override
    public TrxChannel getTrxChannel(int batchGroupId) {
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(batchGroupId);
        long count = trxChannelRepository.findAllByBatchGroup(batchGroup).size();
        int idx = (int)(Math.random() * count);
        Page<TrxChannel> trxChannels = trxChannelRepository.findAllByBatchGroup(batchGroup, PageRequest.of(idx, 1));
        TrxChannel trxChannel = null;
        if (trxChannels.hasContent()) {
            trxChannel = trxChannels.getContent().get(0);
        }
        if (trxChannel == null){
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "channel");
        }
        return trxChannel;
    }

    @Override
    public TrxChannel getTrxChannel(boolean isFtp, int batchGroupId) {
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(batchGroupId);
        long count = trxChannelRepository.findAllByBatchGroup(batchGroup).size();
        List<TrxChannel> trxChannels = trxChannelRepository.findAllByTrxChannelModeAndBatchGroup(isFtp?"FTP":"ISO8583", batchGroup);
        if (trxChannels == null){
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "channel");
        }
        return trxChannels.get(0);
    }
}
