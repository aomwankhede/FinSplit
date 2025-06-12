package aom.finsplit.finsplit.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import aom.finsplit.finsplit.entities.ExpenseGroup;
import aom.finsplit.finsplit.entities.User;

import java.util.List;



public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup,String> {
    List<ExpenseGroup> findByCreator(User creator);
}
