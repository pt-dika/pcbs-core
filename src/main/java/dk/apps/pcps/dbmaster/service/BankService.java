package dk.apps.pcps.dbmaster.service;

import dk.apps.pcps.dbmaster.entity.Bank;
import dk.apps.pcps.model.result.BankData;

public interface BankService {
    BankData getBankData(int bankId);
    Bank getBank(int bankId);
}
