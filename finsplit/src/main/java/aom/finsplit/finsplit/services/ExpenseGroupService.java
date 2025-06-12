package aom.finsplit.finsplit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import aom.finsplit.finsplit.DTO.DTO1;
import aom.finsplit.finsplit.entities.DebtGraph;
import aom.finsplit.finsplit.entities.ExpenseGroup;
import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.interfaces.ExpenseGroupRepository;
import jakarta.transaction.Transactional;

@Service
public class ExpenseGroupService {
    ExpenseGroup expenseGroup;

    @Autowired
    DebtGraph debtGraph;
    
    @Autowired
    ExpenseGroupRepository expenseGroupRepository;

    @Autowired
    UserService userService;

    public List<ExpenseGroup> getAll(){
        return expenseGroupRepository.findAll();
    }

    @Transactional
    public Optional<ExpenseGroup> create(long creatorId,List<Long>others,List<Double>otherAmounts,int totalAmount){
        User creator = userService.getUserFromID(creatorId).get();
        List<DTO1>otherUsers = new ArrayList<>();
        for(int i=0;i<others.size();i++){
            DTO1 D = new DTO1(others.get(i),otherAmounts.get(i),false);
            otherUsers.add(D);
        }
        try {
            ExpenseGroup newExpenseGroup = new ExpenseGroup(creator,otherUsers,totalAmount);
            for (int i=0;i<others.size();i++) {
                debtGraph.addDebt(creatorId,others.get(i),otherAmounts.get(i));
            }
            expenseGroupRepository.save(newExpenseGroup);
            return Optional.of(newExpenseGroup);
        } catch (Exception e) {
            System.out.println("Some error occured" + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    public boolean delete(String id){
        try {
            Optional<ExpenseGroup> e = expenseGroupRepository.findById(id);
            if(e.isEmpty()){
                return false;
            }
            ExpenseGroup e1 = e.get();
            for(DTO1 ele :e1.getOthers()){
                debtGraph.settleDebt(e1.getCreator().getId(),ele.user_id,ele.amount);
            }
            expenseGroupRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID.");
            return false;
        }
        catch(OptimisticLockingFailureException e){
            System.out.println("Heavy Load");
            return false;
        }
    }
}
