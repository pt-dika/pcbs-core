package dk.apps.pcps.main.module;

import dk.apps.pcps.main.model.enums.CardEnum;
import dk.apps.pcps.main.model.payload.ReqPayment;
import dk.apps.pcps.main.model.payload.ReqUpdateBalance;
import dk.apps.pcps.main.model.result.*;

import dk.apps.pcps.main.model.result.fileupload.SettlementFileUploadResult;

public interface PrepaidCardSwitchingService {
    String getAllLogoPrepaid();
    BankInfo getBankInfo(int batchGroupId);
    CardEnum getCard(Integer batchGroupId);
    BinAllowResult getAidBinList();
    TransactionResult doSaveTrx(ReqPayment req);
    TransactionResult getLastTrx(Integer batchGroupId);
    SuccessPaymentResult successPaymentList();
    PaymentResult successPaymentSummary();
    PaymentResult successPaymentDetail(int batchGroupId);
    void generateFileUpload();
    void uploadSettlementFile();
    SettlementFileUploadResult checkFileUpload();
    SettlementSummaryResult doSettlement(Integer batchGroupId);
    void updateBalanceInquiry(CardEnum card, ReqUpdateBalance req);
}
