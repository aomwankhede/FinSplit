package aom.finsplit.finsplit.interfaces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aom.finsplit.finsplit.entities.TransactionLedger;

@Repository
public interface TransactionLedgerRepository extends JpaRepository<TransactionLedger,String> {

}
