package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SettlementDataView;
import dk.apps.pcps.db.repository.SettlementDataViewRepository;
import dk.apps.pcps.db.service.SettlementDataViewService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import dk.apps.pcps.db.function.Global;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SettlementDataViewImpl implements SettlementDataViewService {

    SettlementDataViewRepository settlementDataViewRepository;

    @Autowired
    public SettlementDataViewImpl(SettlementDataViewRepository settlementDataViewRepository){
        this.settlementDataViewRepository = settlementDataViewRepository;
    }

    @Override
    public List<SettlementDataView> getDataForSettlement(SettlementDataView req, int page, int limit, String sortBy) {
        Sort sort = Global.toSort(sortBy);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<SettlementDataView> data = settlementDataViewRepository.findAllByTidAndMidAndUsernameAndBatchGroupAndRefund(req.getTid(), req.getMid(), req.getUsername(), req.getBatchGroup(), false, pageable);
        List<SettlementDataView> successPayments = data.getContent();
        if (successPayments.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);

        return successPayments;
    }

    @Override
    public List<SettlementDataView> getDataForSettlement(String username, int page, int limit, String sortBy) {
        Sort sort = Global.toSort(sortBy);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<SettlementDataView> data = settlementDataViewRepository.findAllByUsernameAndRefund(username, false, pageable);
        List<SettlementDataView> successPayments = data.getContent();
        if (successPayments.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);

        return successPayments;
    }

    @Override
    public List<SettlementDataView> getDataForSettlement(BatchGroup batchGroup, String username) {
        List<SettlementDataView> successPayments = settlementDataViewRepository.findAllByBatchGroupAndUsernameAndRefund(batchGroup, username, false);
        if (successPayments.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);

        return successPayments;
    }

    @Override
    public List<SettlementDataView> getDataForSettlement(BatchGroup batchGroup, String username, int page, int limit, String sortBy) {
        Sort sort = Global.toSort(sortBy);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<SettlementDataView> data = settlementDataViewRepository.findAllByBatchGroupAndUsernameAndRefund(batchGroup, username, false, pageable);
        List<SettlementDataView> successPayments = data.getContent();
        if (successPayments.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);

        return successPayments;
    }

    @Override
    public List<BatchGroup> getBatchGroup(String username) {
        List<SettlementDataView> settlements = settlementDataViewRepository.findAllByUsernameGroupByBatchGroupId(username);
        List<BatchGroup> batchGroups = settlements.stream().map(SettlementDataView::getBatchGroup).collect(Collectors.toList());
        return batchGroups;
    }

    @Override
    public Map countSettlementData(String username, int limit) {
        Sort sort = Global.toSort("DESC:createAt");
        Pageable pageable = PageRequest.of(1, limit, sort);
        Page page = settlementDataViewRepository.findAllByUsernameAndRefund(username, false, pageable);
        Map map = new HashMap();
        map.put("total_data", page.getTotalElements());
        map.put("total_page", page.getTotalPages());
        return map;
    }

    @Override
    public Map countSettlementData(BatchGroup batchGroup, String username, int limit) {
        Sort sort = Global.toSort("DESC:createAt");
        Pageable pageable = PageRequest.of(1, limit, sort);
        Page page = settlementDataViewRepository.findAllByBatchGroupAndUsernameAndRefund(batchGroup, username, false, pageable);
        Map map = new HashMap();
        map.put("total_data", page.getTotalElements());
        map.put("total_page", page.getTotalPages());
        return map;
    }

    @Override
    public String getRandomUser() {
        return null;
    }

    @Override
    public SettlementDataView getRandomData() {
        long count = settlementDataViewRepository.findAllByRefund(false).size();
        int idx = (int)(Math.random() * count);
        Page<SettlementDataView> settlementDataViews = settlementDataViewRepository.findAllByRefund(false, PageRequest.of(idx, 1));
        SettlementDataView successPayment = null;
        if (settlementDataViews.hasContent()) {
            successPayment = settlementDataViews.getContent().get(0);
        }
        if (successPayment == null){
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "no data for settlements");
        }
        return successPayment;
    }

    @Override
    public SettlementDataView getRandomData(String username) {
        long count = settlementDataViewRepository.findAllByUsernameAndRefund(username, false).size();
        int idx = (int)(Math.random() * count);
        Page<SettlementDataView> settlementDataViews = settlementDataViewRepository.findAllByUsernameAndRefund(username,false, PageRequest.of(idx, 1));
        SettlementDataView successPayment = null;
        if (settlementDataViews.hasContent()) {
            successPayment = settlementDataViews.getContent().get(0);
        }
        if (successPayment == null){
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
        }
        return successPayment;
    }
}
