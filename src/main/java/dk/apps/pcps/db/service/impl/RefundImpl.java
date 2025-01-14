package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.Refund;
import dk.apps.pcps.db.repository.RefundRepository;
import dk.apps.pcps.db.service.RefundService;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dk.apps.pcps.db.function.Global;


import dk.apps.pcps.main.handler.ApplicationException;

import java.util.List;

@Service
public class RefundImpl implements RefundService {

    RefundRepository refundRepository;

    @Autowired
    public RefundImpl(RefundRepository refundRepository){
        this.refundRepository = refundRepository;
    }

    @Override
    public List<Refund> getDataForSettlement(Refund req, int page, int limit, String sortBy) {
        Sort sort = Global.toSort(sortBy);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Refund> data = refundRepository.findAllByTidAndMidAndUsernameAndBatchGroup(req.getTid(), req.getMid(), req.getUsername(), req.getBatchGroup(), pageable);
        List<Refund> refunds = data.getContent();
        if (refunds.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);

        return refunds;
    }

    @Override
    public Refund getDataRefund(int invoiceNumber,String username) {
        return refundRepository.findByInvoiceNumAndUsername(invoiceNumber, username)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
    }

    @Override
    public Refund getRandomData() {
        int count = refundRepository.findAll().size();
        int idx = (int)(Math.random() * count);
        Page<Refund> refunds = refundRepository.findAll(PageRequest.of(idx, 1));
        Refund refund = null;
        if (refunds.hasContent()) {
            refund = refunds.getContent().get(0);
        }
        return refund;
    }

    @Override
    public Refund createDataRefund(Refund refund) {
        return refundRepository.save(refund);
    }

    @Override
    public void removeAllDataBatch(List<Integer> ids) {
        List<Refund> successPayments = refundRepository.findAllById(ids);
        if (successPayments.size() > 0) {
            refundRepository.deleteAll(successPayments);
        } else
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
    }
}
