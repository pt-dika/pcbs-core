package dk.apps.pcps.main.module;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.main.model.payload.ReqRangeHistory;
import dk.apps.pcps.main.model.result.PaymentResult;
import dk.apps.pcps.main.model.result.TrxHistoryResult;


public interface ReportService {
    PaymentResult settlementSummary(int batchGroupId);
    void settlementSessionHistory(BatchGroup batchGroup, ReqRangeHistory req);
    PaymentResult settlementSummaryHistory(ReqRangeHistory reqRangeHistory);
    PaymentResult settlementDetailHistory(int sessionNumber);
    TrxHistoryResult last10TrxHistory();
}
