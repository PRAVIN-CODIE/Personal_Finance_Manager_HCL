package com.finance.controller;
 
import com.finance.model.Transaction;
import com.finance.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
 
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
 
    @Autowired private TransactionRepo repo;
 
    @GetMapping
    public List<Transaction> getAll() { return repo.findAll(); }
 
    @GetMapping("/{id}")
    public Transaction getOne(@PathVariable Long id) { return repo.findById(id).orElse(null); }
 
    @PostMapping
    public Transaction create(@RequestBody Transaction t) {
        if (t.getDate() == null) t.setDate(LocalDate.now());
        return repo.save(t);
    }
 
    @PutMapping("/{id}")
    public Transaction update(@PathVariable Long id, @RequestBody Transaction t) {
        t.setId(id);
        return repo.save(t);
    }
 
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "Deleted " + id;
    }
 
    // Reports
    @GetMapping("/report/monthly")
    public Map<String, Double> monthlyReport() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();
        return repo.findByDateBetween(start, end).stream()
            .filter(t -> "EXPENSE".equals(t.getType()))
            .collect(Collectors.groupingBy(Transaction::getCategory,
                    Collectors.summingDouble(Transaction::getAmount)));
    }
 
    @GetMapping("/report/summary")
    public Map<String, Double> summary() {
        List<Transaction> all = repo.findAll();
        double income = all.stream().filter(t -> "INCOME".equals(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
        double expense = all.stream().filter(t -> "EXPENSE".equals(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
        Map<String, Double> m = new HashMap<>();
        m.put("income", income);
        m.put("expense", expense);
        m.put("balance", income - expense);
        return m;
    }
}
