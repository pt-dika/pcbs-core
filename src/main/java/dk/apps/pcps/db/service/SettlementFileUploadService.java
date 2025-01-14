package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.SettlementFileUpload;
import dk.apps.pcps.main.model.result.SuccessPaymentSummary;
import dk.apps.pcps.main.model.result.fileupload.SettlementFileUploadData;

import java.util.List;

public interface SettlementFileUploadService {
    void createData(SettlementFileUpload settlementFileUpload);
    void generateFile();
    void uploadFile();
    List<SettlementFileUploadData> checkResponseFile();
    List<SuccessPaymentSummary> getSettlementSummary(String username, Integer batchGroupId);
    void createSettlementData();
}
