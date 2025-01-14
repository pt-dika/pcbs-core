package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BinParent;

import java.util.List;

public interface BinParentListService {

    List<BinParent> getAllBinParent(List<Integer> ids);
    List<BinParent> getAllBinParent();
    BinParent getBinParent(int id);
}
