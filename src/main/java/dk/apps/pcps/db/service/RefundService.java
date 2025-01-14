package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.Refund;

import java.util.List;

public interface RefundService {
    List<Refund> getDataForSettlement(Refund refund, int page, int limit, String sortBy);
    Refund getDataRefund(int invoiceNumber, String username);
    Refund getRandomData();
    Refund createDataRefund(Refund refund);
    void removeAllDataBatch(List<Integer> ids);
}
