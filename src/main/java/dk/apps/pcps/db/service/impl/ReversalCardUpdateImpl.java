package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.ObjectMapper;
import dk.apps.pcps.db.entity.ReversalCardUpdate;
import dk.apps.pcps.db.repository.CardUpdateInquiryRepository;
import dk.apps.pcps.db.service.ReversalCardUpdateService;
import dk.apps.pcps.main.model.result.ReversalCardUpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReversalCardUpdateImpl implements ReversalCardUpdateService {

    CardUpdateInquiryRepository cardUpdateInquiryRepository;

    @Autowired
    public ReversalCardUpdateImpl(CardUpdateInquiryRepository cardUpdateInquiryRepository) {
        this.cardUpdateInquiryRepository = cardUpdateInquiryRepository;
    }

    @Override
    public ReversalCardUpdateData getReversal(String username) {
        ReversalCardUpdate inquiry = cardUpdateInquiryRepository.findByUsername(username);
        if (inquiry != null)
            return ObjectMapper.map(inquiry, ReversalCardUpdateData.class);
        else
            return null;
    }

    @Override
    public ReversalCardUpdateData createReversal(ReversalCardUpdateData data) {
        ReversalCardUpdate reversalCardUpdate = ObjectMapper.map(data, ReversalCardUpdate.class);
        reversalCardUpdate = cardUpdateInquiryRepository.save(reversalCardUpdate);
        return ObjectMapper.map(reversalCardUpdate, ReversalCardUpdateData.class);
    }

    @Override
    public void removeReversal(String username) {
        ReversalCardUpdateData inquiry = getReversal(username);
        ReversalCardUpdate reversalCardUpdate = ObjectMapper.map(inquiry, ReversalCardUpdate.class);
        cardUpdateInquiryRepository.delete(reversalCardUpdate);
    }
}
