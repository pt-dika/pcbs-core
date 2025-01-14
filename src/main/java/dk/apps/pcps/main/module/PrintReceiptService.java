package dk.apps.pcps.main.module;

import dk.apps.pcps.model.payload.PrintReceipt;
import dk.apps.pcps.model.result.PrintReceiptData;
import dk.apps.pcps.model.result.receipt.ReceiptTemplate;

import java.io.IOException;

public interface PrintReceiptService {
    PrintReceiptData saleReceipt(int batchGroupId, int invoiceNum);
    ReceiptTemplate saleReceiptData(int batchGroupId, int invoiceNum);
    ReceiptTemplate settlementReceiptData(int batchGroupId, int sessionNum) throws IOException;
    PrintReceiptData settlementReceipt(int batchGroupId, int sessionNum) throws IOException;
    PrintReceiptData lastSettlementReceipt(int batchGroupId) throws IOException;
    PrintReceiptData saleReceipt(PrintReceipt printReceipt) throws IOException;
    PrintReceiptData settlementReceipt(PrintReceipt printReceipt);
}
