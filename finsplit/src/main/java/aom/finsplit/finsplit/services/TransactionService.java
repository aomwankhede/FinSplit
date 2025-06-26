package aom.finsplit.finsplit.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aom.finsplit.finsplit.interfaces.TransactionLedgerRepository;
import lombok.extern.slf4j.Slf4j;
import aom.finsplit.finsplit.entities.TransactionLedger;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    TransactionLedgerRepository transactionalRepository;

    public List<TransactionLedger> getAll() {
        return transactionalRepository.findAll();
    }

    public Optional<TransactionLedger> getById(String id) {
        return transactionalRepository.findById(id);
    }

    public boolean create(long payerId, long payeeId, double amount, boolean res) {
        try {
            TransactionLedger newTransationLedger = new TransactionLedger(payerId, payeeId, amount, res);
            transactionalRepository.save(newTransationLedger);
            log.info("Transaction created successfully");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void delete(String id) {
        try {
            transactionalRepository.deleteById(id);
            log.info("Transaction deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting transaction with ID {}: {}", id, e.getMessage());
        }
    }
}