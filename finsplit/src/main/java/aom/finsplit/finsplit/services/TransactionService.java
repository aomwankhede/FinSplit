package aom.finsplit.finsplit.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aom.finsplit.finsplit.interfaces.TransactionLedgerRepository;
import aom.finsplit.finsplit.entities.TransactionLedger;

@Service
public class TransactionService {
    @Autowired
    TransactionLedgerRepository transactionalRepository;
    
    public List<TransactionLedger> getAll(){
        return transactionalRepository.findAll();
    }

    public Optional<TransactionLedger> getById(String id){
        return transactionalRepository.findById(id);
    }

    public boolean create(long payerId,long payeeId,double amount,boolean res){
        try {
            TransactionLedger newTransationLedger = new TransactionLedger(payerId,payeeId,amount,res);
            transactionalRepository.save(newTransationLedger);
            return true;
        } catch (Exception e) {
            System.out.println("Some error occured " + e.getMessage());
            return false;
        }
    }

    public void delete(String id){
        transactionalRepository.deleteById(id);
    }
}