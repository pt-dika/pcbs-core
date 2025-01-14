package dk.apps.pcps.main.module.brizzi;

import dk.apps.pcps.main.model.result.SettlementSummaryResult;

import java.util.Map;

public interface BrizziService {
    SettlementSummaryResult doSettlement();
    Map generateSettlementData();
}
