package dk.apps.pcps.main.module.flazz;

import dk.apps.pcps.main.model.result.SettlementSummaryResult;

import java.util.Map;

public interface FlazzService {

    SettlementSummaryResult doSettlement();
    Map generateSettlementData();
}
