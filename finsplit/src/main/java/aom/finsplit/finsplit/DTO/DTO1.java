package aom.finsplit.finsplit.DTO;
import jakarta.persistence.Embeddable;

@Embeddable
public class DTO1 {
    public long user_id;
    public boolean paid;
    public Double amount;
    public DTO1(){

    }
    public DTO1(long user_id,Double amount,boolean paid){
        this.user_id = user_id;
        this.paid = paid;
        this.amount = amount;
    }
}