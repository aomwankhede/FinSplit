package aom.finsplit.finsplit.entities;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import aom.finsplit.finsplit.DTO.DTO1;

@Entity
public class ExpenseGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private User creator;

    @ElementCollection
    private List<DTO1> others;

    @Min(1)
    int totalAmount;

    public ExpenseGroup() {
    }

    public ExpenseGroup(User creator, List<DTO1> others, int totalAmount) {
        this.creator = creator;
        this.others = others;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return this.id;
    }

    public User getCreator() {
        return this.creator;
    }

    public List<DTO1> getOthers() {
        return this.others;
    }

    public int getTotalAmount() {
        return this.totalAmount;
    }
}
