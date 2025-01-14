package dk.apps.pcps.db.service;


import dk.apps.pcps.db.entity.BinChild;

import java.util.List;

public interface BinChildListService {

    List<BinChild> getAllBinChild(List<Integer> ids);
    List<BinChild> getAllBinChild();
    BinChild getBinChild(int id);
}
