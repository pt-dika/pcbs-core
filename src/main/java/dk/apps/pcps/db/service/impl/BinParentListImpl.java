package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BinParent;
import dk.apps.pcps.db.repository.BinParentRepository;
import dk.apps.pcps.db.service.BinParentListService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




import java.util.List;

@Component
public class BinParentListImpl implements BinParentListService {

    BinParentRepository binParentRepository;

    @Autowired
    public BinParentListImpl(BinParentRepository binParentRepository){
        this.binParentRepository = binParentRepository;
    }

    @Override
    public List<BinParent> getAllBinParent(List<Integer> ids) {
        return binParentRepository.findAllById(ids);
    }

    @Override
    public List<BinParent> getAllBinParent() {
        return binParentRepository.findAll();
    }

    @Override
    public BinParent getBinParent(int id) {
        return binParentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
    }
}
