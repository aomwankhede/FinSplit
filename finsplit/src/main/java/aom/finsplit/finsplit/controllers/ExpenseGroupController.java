package aom.finsplit.finsplit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aom.finsplit.finsplit.entities.ExpenseGroup;
import aom.finsplit.finsplit.services.ExpenseGroupService;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/expense")
public class ExpenseGroupController {
    @Autowired
    ExpenseGroupService expenseGroupService;

    @GetMapping("/allexpenses")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(expenseGroupService.getAll());
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, ?> body) {
        try{
            long creatorId = Long.parseLong(String.valueOf(body.get("creatorId")));
            List<Integer> othersInt = (List<Integer>)body.get("others");
            List<Long> others = othersInt.stream().map(id->(long)id).toList();
            List<Double> otherAmount;
            int totalAmount = Integer.parseInt(String.valueOf(body.get("totalAmount")));
            if (body.get("otherAmount") != null) {
                otherAmount = (List<Double>) body.get("otherAmount");
            } else {
                double value = ((double) totalAmount) / (others.size() + 1);
                otherAmount = Stream.iterate(value, n -> n).limit(others.size()).toList();
            }
            ExpenseGroup newExpenseGroup = expenseGroupService.create(creatorId, others, otherAmount, totalAmount).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(newExpenseGroup);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean flag = expenseGroupService.delete(id);
        if (!flag) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cant be deleted");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
    }
}
