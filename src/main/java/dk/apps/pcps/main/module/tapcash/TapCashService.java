package dk.apps.pcps.main.module.tapcash;


import dk.apps.pcps.main.model.payload.ReqLogon;
import dk.apps.pcps.main.model.payload.ReqUpdateBalance;
import dk.apps.pcps.main.model.payload.RespUnPair;
import dk.apps.pcps.main.module.tapcash.model.InquiryUpdateBalanceResult;
import dk.apps.pcps.main.module.tapcash.model.LogonData;
import dk.apps.pcps.main.module.tapcash.model.SamData;
import dk.apps.pcps.main.module.tapcash.model.SettlementMessage;

import dk.apps.pcps.main.model.result.SettlementSummaryResult;

import java.util.List;
import java.util.Map;

public interface TapCashService {
    void saveTMK(int batchGroupId, String seed, String tmk, String mtmkIndex);
    LogonData logonProcess(ReqLogon req, boolean isSettlement);
    Boolean logoff(ReqLogon req);
    SettlementSummaryResult doSettlement(String username);
    List<SettlementMessage> generateSettlementMessages(int sessionNum);
    List<Map> generateSettlementData(int countUpload);
    boolean uploadSettlementData();
    boolean checkSettlement();
    InquiryUpdateBalanceResult updateBalanceInquiry(ReqUpdateBalance req);
    InquiryUpdateBalanceResult cardUpdateReversal(ReqUpdateBalance req);
    SamData createOrUpdateMarryCode(String marriageCode);
    void unMarrySuccess(String unPairCode, String extAuthCode);
    RespUnPair getUnMarryCode(String samId, String crn, String trn);
    RespUnPair generateUnMarryCode(String samId, String crn, String trn);
}
