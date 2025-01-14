package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BinChild;
import dk.apps.pcps.db.repository.BinChildRepository;
import dk.apps.pcps.db.service.BinChildListService;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import dk.apps.pcps.main.handler.ApplicationException;

import java.util.List;

@Component
public class BinChildListImpl implements BinChildListService {

    BinChildRepository binChildRepository;

    @Autowired
    public BinChildListImpl(BinChildRepository binChildRepository){
        this.binChildRepository = binChildRepository;
    }

    @Override
    public List<BinChild> getAllBinChild(List<Integer> ids) {
        return binChildRepository.findAllById(ids);
    }

    @Override
    public List<BinChild> getAllBinChild() {
        return binChildRepository.findAll();
    }

    @Override
    public BinChild getBinChild(int id) {
        return binChildRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
    }
}
