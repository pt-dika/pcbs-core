package dk.apps.pcps.dbmaster.service.impl;

import dk.apps.pcps.commonutils.ObjectMapper;
import dk.apps.pcps.dbmaster.entity.Bank;
import dk.apps.pcps.dbmaster.repository.BankRepository;
import dk.apps.pcps.dbmaster.service.BankService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.result.BankData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankImpl implements BankService {

    BankRepository bankRepository;

    @Autowired
    private BankImpl(BankRepository bankRepository){
        this.bankRepository = bankRepository;
    }

    @Override
    public BankData getBankData(int bankId) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
        return ObjectMapper.map(bank, BankData.class);
    }

    @Override
    public Bank getBank(int bankId) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
        return bank;
    }
}
