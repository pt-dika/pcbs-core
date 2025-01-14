package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SuccessPayment;
import dk.apps.pcps.main.model.result.SuccessPaymentDetail;

import java.util.List;

public interface SuccessPaymentService {

    List<SuccessPayment> getDataForSettlement(SuccessPayment req, int page, int limit, String sortBy);
    List<SuccessPayment> getSuccessPayment(String username);
    List<SuccessPayment> getSuccessPayments(String username, BatchGroup batchGroup, int[] invoiceNums);
    List<BatchGroup> getBatchGroup(String username);
    SuccessPaymentDetail getSuccessPaymentDetail(int invoiceNumber, String username);
    List<SuccessPaymentDetail> getSuccessPaymentDetails(String username, BatchGroup batchGroup);
    List<SuccessPaymentDetail> getSuccessPaymentDetails(String username, BatchGroup batchGroup, int page, int nums);
    SuccessPayment getRandomData();
    SuccessPayment updateStatusPaymentToRefund(SuccessPayment successPayment);
    SuccessPayment getSuccessPaymentData(int invoiceNumber, String username);
    SuccessPayment createTransactionData(SuccessPayment successPayment);
    SuccessPaymentDetail createTransaction(SuccessPayment successPayment);
    List<SuccessPayment> createTransactions(List<SuccessPayment> successPayment);
    void removeAllDataBatch(List<Integer> ids);
}
