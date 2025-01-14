package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.ObjectMapper;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.Settlement;
import dk.apps.pcps.db.repository.SettlementRepository;
import dk.apps.pcps.db.service.SettlementService;
import dk.apps.pcps.db.service.SuccessPaymentService;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.result.SuccessPaymentDetail;
import dk.apps.pcps.model.result.BatchGroupData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




import java.util.List;
import java.util.stream.Collectors;

@Component
public class SettlementImpl implements SettlementService {

    AuthService authService;
    SuccessPaymentService successPaymentService;
    SettlementRepository settlementRepository;

    @Autowired
    public SettlementImpl(AuthService authService, SuccessPaymentService successPaymentService, SettlementRepository settlementRepository){
        this.authService = authService;
        this.successPaymentService = successPaymentService;
        this.settlementRepository = settlementRepository;
    }

    @Override
    public List<Integer> getSessionNumbers(String username, String from, String to) {
        List<Settlement> settlements;
        if (from.isEmpty() || to.isEmpty())
            settlements = settlementRepository.findAllByUsernameGroupBySessNum(username);
        else
            settlements = settlementRepository.findAllByUsernameGroupBySessNum(username, from, to);
        return settlements
                .stream()
                .map(Settlement::getSettlementSessionNum)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSessionNumbers(String username, BatchGroup batchGroup, String from, String to) {
        List<Settlement> settlements;
        if (from.isEmpty() || to.isEmpty())
            settlements = settlementRepository.findAllByUsernameGroupBySessNum(username);
        else
            settlements = settlementRepository.findAllByUsernameGroupBySessNum(username, from, to);
        return settlements
                .stream()
                .map(Settlement::getSettlementSessionNum)
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchGroup> getBatchGroup(String username, Integer sessNum) {
        List<Settlement> settlements = settlementRepository.findAllByUsernameAndSessionNumGroupByBatchGroupId(username, sessNum);
        if (settlements.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
        List<BatchGroup> batchGroups = settlements.stream().map(Settlement::getBatchGroup).collect(Collectors.toList());
        return batchGroups;
    }

    @Override
    public Settlement getLastSettlement(String username, Integer batchGroupId) {
        return settlementRepository.findFirstByUsernameAndBatchGroupIdOrderByCreateAtDesc(username, batchGroupId);
    }

    @Override
    public Settlement getSettlementData(Integer batchGroupId, Integer sessionNum) {
        MobileAppUsers appUsers = authService.getAuthUsers();
        String username = appUsers.getUsername();
        return settlementRepository.findFirstByUsernameAndBatchGroupIdAndSettlementSessionNum(username, batchGroupId, sessionNum);
    }

    @Override
    public List<Settlement> getDataSettlements(String username, Integer sessionNumber, Integer batchGroupId) {
        return settlementRepository.findAllByUsernameAndSettlementSessionNumAndBatchGroupId(username, sessionNumber, batchGroupId);
    }

    @Override
    public List<Settlement> getDataSettlements(String username, Integer sessionNumber) {
        return settlementRepository.findAllByUsernameAndSettlementSessionNum(username, sessionNumber);
    }

    @Override
    public List<Settlement> getDataSettlements(String username) {
        return settlementRepository.findAllByUsername(username);
    }

    @Override
    public Settlement getDataSettlement(int batchNumber, String username) {
        return settlementRepository.findByBatchNumAndUsername(batchNumber, username)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
    }

    @Override
    public Settlement createDataSettlement(Settlement settlement) {
        return settlementRepository.save(settlement);
    }

    @Override
    public BatchGroupData settlementSummary(BatchGroup batchGroup) {
        MobileAppUsers appUsers = authService.getAuthUsers();
        String username = appUsers.getUsername();
        BatchGroupData batchGroupData = ObjectMapper.map(batchGroup, BatchGroupData.class);
        List<SuccessPaymentDetail> successPayments = successPaymentService.getSuccessPaymentDetails(username, batchGroup);
        int numOfTransaction = successPayments.size();
        long totalBaseAmount = successPayments.stream().filter(x -> x.getBaseAmount() > successPayments.size()).mapToLong(x -> x.getBaseAmount()).sum();
        batchGroupData.setTotalAmount(totalBaseAmount);
        batchGroupData.setTrxCount(numOfTransaction);
        return batchGroupData;
    }
}
