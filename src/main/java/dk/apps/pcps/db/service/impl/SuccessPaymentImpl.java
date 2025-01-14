package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.ObjectMapper;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SuccessPayment;
import dk.apps.pcps.db.repository.SuccessPaymentRepository;
import dk.apps.pcps.db.service.SuccessPaymentService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.result.SuccessPaymentData;
import dk.apps.pcps.main.model.result.SuccessPaymentDetail;
import dk.apps.pcps.main.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import dk.apps.pcps.db.function.Global;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuccessPaymentImpl implements SuccessPaymentService {

    SuccessPaymentRepository successPaymentRepository;

    @Autowired
    public SuccessPaymentImpl(SuccessPaymentRepository successPaymentRepository){
        this.successPaymentRepository = successPaymentRepository;
    }

    @Override
    public List<SuccessPayment> getDataForSettlement(SuccessPayment req, int page, int limit, String sortBy) {
        Sort sort = Global.toSort(sortBy);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<SuccessPayment> data = successPaymentRepository.findAllByTidAndMidAndUsernameAndBatchGroup(req.getTid(), req.getMid(), req.getUsername(), req.getBatchGroup(), pageable);
        List<SuccessPayment> successPayments = data.getContent();
        if (successPayments.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);

        return successPayments;
    }

    @Override
    public List<SuccessPayment> getSuccessPayment(String username) {
        return successPaymentRepository.findAllByUsername(username);
    }

    @Override
    public List<SuccessPayment> getSuccessPayments(String username, BatchGroup batchGroup, int[] invoiceNums) {
        return successPaymentRepository.findAllByUsernameAndBatchGroupAndInvoiceNumInOrderByCreateAtDesc(username, batchGroup, invoiceNums);
    }

    @Override
    public List<BatchGroup> getBatchGroup(String username) {
        List<SuccessPayment> settlements = successPaymentRepository.findAllByUsernameGroupByBatchGroupId(username);
        List<BatchGroup> batchGroups = settlements.stream().map(SuccessPayment::getBatchGroup).collect(Collectors.toList());
        return batchGroups;
    }

    @Override
    public List<SuccessPaymentDetail> getSuccessPaymentDetails(String username, BatchGroup batchGroup) {
        List<SuccessPayment> successPayments = successPaymentRepository.findAllByUsernameAndBatchGroupOrderByCreateAtDesc(username, batchGroup);
        List<SuccessPaymentDetail> successPaymentDetails = ObjectMapper.mapAll(successPayments, SuccessPaymentDetail.class);
        successPaymentDetails = convertDetails(successPaymentDetails);
        return successPaymentDetails;
    }

    @Override
    public List<SuccessPaymentDetail> getSuccessPaymentDetails(String username, BatchGroup batchGroup, int page, int nums) {
        Sort sort = Global.toSort("DESC:createAt");
        Pageable pageable = PageRequest.of(1, nums, sort);
        List<SuccessPayment> successPayments;
        if (batchGroup == null)
            successPayments = successPaymentRepository.findAllByUsername(username, pageable).getContent();
        else
            successPayments = successPaymentRepository.findAllByUsernameAndBatchGroup(username, batchGroup, pageable).getContent();

        if (successPayments.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
        List<SuccessPaymentDetail> successPaymentDetails = ObjectMapper.mapAll(successPayments, SuccessPaymentDetail.class);
        successPaymentDetails = convertDetails(successPaymentDetails);
        return successPaymentDetails;
    }

    @Override
    public SuccessPayment getRandomData() {
        int count = successPaymentRepository.findAll().size();
        int idx = (int)(Math.random() * count);
        Page<SuccessPayment> successPayments = successPaymentRepository.findAll(PageRequest.of(idx, 1));
        SuccessPayment successPayment = null;
        if (successPayments.hasContent()) {
            successPayment = successPayments.getContent().get(0);
        }
        return successPayment;
    }

    @Override
    public SuccessPayment updateStatusPaymentToRefund(SuccessPayment successPayment) {
        return successPaymentRepository.save(successPayment);
    }

    @Override
    public SuccessPayment getSuccessPaymentData(int invoiceNumber, String username) {
        return null;
    }

    @Override
    public SuccessPaymentDetail getSuccessPaymentDetail(int invoiceNumber, String username) {
        SuccessPayment successPayment = successPaymentRepository.findByInvoiceNumAndUsername(invoiceNumber, username);
        if (successPayment == null)
            return null;
        SuccessPaymentDetail successPaymentDetail = ObjectMapper.map(successPayment, SuccessPaymentDetail.class);
        successPaymentDetail.setInvoiceNumber(ISOConverters.padLeftZeros(""+successPayment.getInvoiceNum(), 6));
        return successPaymentDetail;
    }

    @Override
    public SuccessPayment createTransactionData(SuccessPayment successPayment) {
        return null;
    }

    @Override
    public SuccessPaymentDetail createTransaction(SuccessPayment successPayment) {
        successPayment = successPaymentRepository.save(successPayment);
        SuccessPaymentDetail successPaymentDetail = ObjectMapper.map(successPayment, SuccessPaymentDetail.class);
        successPaymentDetail.setInvoiceNumber(ISOConverters.padLeftZeros(""+successPayment.getInvoiceNum(), 6));
        return successPaymentDetail;
    }

    @Override
    public List<SuccessPayment> createTransactions(List<SuccessPayment> successPayment) {
        return successPaymentRepository.saveAll(successPayment);
    }

    @Override
    public void removeAllDataBatch(List<Integer> ids) {
        List<SuccessPayment> successPayments = successPaymentRepository.findAllByInvoiceNumIn(ids);
        if (successPayments.size() > 0) {
            successPaymentRepository.deleteAll(successPayments);
        } else
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
    }

    private List<SuccessPaymentData> invoiceNumToString(List<SuccessPaymentData> successPaymentDataList){
        successPaymentDataList.forEach(
                f -> {
                    f.setInvoiceNumber(ISOConverters.padLeftZeros(""+f.getInvoiceNum(), 6));
                }
        );
        return successPaymentDataList;
    }

    private List<SuccessPaymentDetail> convertDetails(List<SuccessPaymentDetail> successPaymentDetails){
        successPaymentDetails.forEach(
                f -> {
                    f.setInvoiceNumber(ISOConverters.padLeftZeros(""+f.getInvoiceNum(), 6));
                    f.setCan(Utility.maskedCan(f.getCan()));
                }
        );
        return successPaymentDetails;
    }
}
