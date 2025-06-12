package aom.finsplit.finsplit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;

@Entity
public class TransactionLedger {
    private long payeeId;
    private long payerId;
    private double amount;
    private boolean result;
    private long timestamp;
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    public TransactionLedger() {
    }

    public TransactionLedger(long payerId, long payeeId, double amount,boolean result) {
        this.payeeId = payeeId;
        this.payerId = payerId;
        this.amount = amount;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }

    public long getPayeeId() {
        return this.payeeId;
    }

    public long getPayerId() {
        return this.payerId;
    }

    @Min(0)
    public double getAmount() {
        return this.amount;
    }

    public boolean getResult(){
        return this.result;
    }

    public long getTimeStamp(){
        return this.timestamp;
    }
}
