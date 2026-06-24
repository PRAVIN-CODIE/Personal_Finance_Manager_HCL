package com.finance.controller;
 
import com.finance.model.Budget;
import com.finance.model.Transaction;
import com.finance.repo.BudgetRepo;
import com.finance.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
 
@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
 
    @Autowired private BudgetRepo repo;
    @Autowired private TransactionRepo txRepo;
 
    @GetMapping public List<Budget> all() { return repo.findAll(); }
 
    @GetMapping("/{id}") public Budget one(@PathVariable Long id) { return repo.findById(id).orElse(null); }
 
    @PostMapping public Budget create(@RequestBody Budget b) { return repo.save(b); }
 
    @PutMapping("/{id}")
    public Budget update(@PathVariable Long id, @RequestBody Budget b) {
        b.setId(id); return repo.save(b);
    }
 
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "Deleted"; }
 
    @GetMapping("/status")
    public List<Map<String,Object>> status() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        List<Transaction> txs = txRepo.findByDateBetween(start, LocalDate.now());
        List<Map<String,Object>> result = new ArrayList<>();
        for (Budget b : repo.findAll()) {
            double spent = txs.stream()
                .filter(t -> b.getCategory().equalsIgnoreCase(t.getCategory()) && "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
            double pct = b.getMonthlyLimit() == 0 ? 0 : (spent / b.getMonthlyLimit()) * 100;
            Map<String,Object> m = new HashMap<>();
            m.put("category", b.getCategory());
            m.put("limit", b.getMonthlyLimit());
            m.put("spent", spent);
            m.put("percent", Math.round(pct));
            m.put("alert", pct >= 80 ? "You have used " + Math.round(pct) + "% of your " + b.getCategory() + " budget" : null);
            result.add(m);
        }
        return result;
    }
}
 
