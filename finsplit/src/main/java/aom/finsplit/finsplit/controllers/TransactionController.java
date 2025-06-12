package aom.finsplit.finsplit.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aom.finsplit.finsplit.entities.DebtGraph;
import aom.finsplit.finsplit.services.TransactionService;
import aom.finsplit.finsplit.services.UserService;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    DebtGraph debtGraph;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transationService;

    @PostMapping("/simulate")
    public ResponseEntity<?> simulateTransaction(@RequestBody String NOUSE) throws InterruptedException {
        Map<String, Boolean> map = new HashMap<>();
        map.put("result", Math.random() < 0.2);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PostMapping("/post-processing")
    @Transactional
    public ResponseEntity<?> postTransactionProcessing(@RequestBody Map<String, String> body) {
        long creatorId = Long.parseLong(body.get("creatorId"));
        long othersId = Long.parseLong(body.get("othersId"));
        double amount = Double.parseDouble(body.get("amount"));
        boolean result = "true".equals(body.get("result"));
        boolean success = transationService.create(creatorId, othersId, amount, result);
        if (success) {
            if (result) {
                boolean flag = debtGraph.settleDebt(creatorId, othersId, amount);
                if (flag) {
                    Map<String, String> mp1 = new HashMap<>();
                    mp1.put("walletBalance",
                            Integer.toString(
                                    userService.getUserFromID(creatorId).get().getWalletBalance() - (int) amount));
                    userService.updateUser(creatorId, mp1);
                    Map<String, String> mp2 = new HashMap<>();
                    mp2.put("walletBalance",
                            Integer.toString(
                                    userService.getUserFromID(othersId).get().getWalletBalance() + (int) amount));
                    userService.updateUser(othersId, mp2);
                    return ResponseEntity.status(HttpStatus.OK).body("Post Processing done successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Some error occured.");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("Post processing done succefully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Post processing failed");
        }
    }

    @GetMapping("/get-money/{id}")
    public ResponseEntity<?> toGetMoney(@PathVariable long id) {
        // Debt is not a table
        return ResponseEntity.status(HttpStatus.OK).body(debtGraph.getDebtsByUser(id));
    }

    @GetMapping("/give-money/{id}")
    public ResponseEntity<?> toGiveMoney(@PathVariable long id) {
        // Debt is not a table
        return ResponseEntity.status(HttpStatus.OK).body(debtGraph.getOwesById(id));
    }
}