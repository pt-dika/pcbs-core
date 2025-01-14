package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BinAndBatchGroupTemplateList;
import dk.apps.pcps.db.entity.MerchantDetail;
import dk.apps.pcps.db.repository.BinAndBatchGroupTemplateListRepository;
import dk.apps.pcps.db.service.BinAndBatchGroupTemplateListService;
import dk.apps.pcps.db.service.MerchantDetailService;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import org.springframework.stereotype.Service;




import java.util.List;


@Service
public class BinAndBatchGroupTemplateListServiceImpl implements BinAndBatchGroupTemplateListService {

    AuthService authService;
    MerchantDetailService merchantDetailService;
    BinAndBatchGroupTemplateListRepository binAndBatchGroupTemplateListRepository;

    public BinAndBatchGroupTemplateListServiceImpl(AuthService authService,
                                                   MerchantDetailService merchantDetailService,
                                                   BinAndBatchGroupTemplateListRepository binAndBatchGroupTemplateListRepository){
        this.authService = authService;
        this.merchantDetailService = merchantDetailService;
        this.binAndBatchGroupTemplateListRepository = binAndBatchGroupTemplateListRepository;
    }


    @Override
    public BinAndBatchGroupTemplateList getBatchGroupTemplate(int batchGroupTemplateListId, int batchGroupId) {
        return binAndBatchGroupTemplateListRepository.findByBinIdAndBatchGroupListTemplate_Id(batchGroupId, batchGroupTemplateListId);
    }

    @Override
    public List<BinAndBatchGroupTemplateList> getTemplateList() {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        MerchantDetail merchantDetail = merchantDetailService.getMerchantDetail(mobileAppUsers.getMerchant().getId());
        return getTemplateList(merchantDetail.getBatchGroupListTemplate().getId());
    }

    @Override
    public List<BinAndBatchGroupTemplateList> getTemplateList(int batchGroupTemplateListId) {
        return binAndBatchGroupTemplateListRepository.findAllByBatchGroupListTemplate_IdAndIsParentFlagTrue(batchGroupTemplateListId);
    }

    @Override
    public List<BinAndBatchGroupTemplateList> getTemplateListChild(int batchGroupTemplateListId) {
        return binAndBatchGroupTemplateListRepository.findAllByBatchGroupListTemplate_IdAndIsParentFlagFalse(batchGroupTemplateListId);
    }

    @Override
    public BinAndBatchGroupTemplateList getTemplateList(int binId, int batchGroupTemplateListId) {
        return binAndBatchGroupTemplateListRepository.findByBinIdAndBatchGroupListTemplate_Id(binId,batchGroupTemplateListId);
    }
}
