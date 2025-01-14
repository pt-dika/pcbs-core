package dk.apps.pcps.main.module.emoney;

import dk.apps.pcps.main.model.result.SettlementSummaryResult;

import java.util.Map;

public interface EMoneyService {

    SettlementSummaryResult doSettlement();
    Map generateSettlementData();
}
