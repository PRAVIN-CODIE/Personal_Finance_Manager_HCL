package com.finance.controller;
 
import com.finance.model.SavingsGoal;
import com.finance.repo.SavingsGoalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
 
@RestController
@RequestMapping("/api/goals")
public class SavingsGoalController {
 
    @Autowired private SavingsGoalRepo repo;
 
    @GetMapping public List<SavingsGoal> all() { return repo.findAll(); }
    @GetMapping("/{id}") public SavingsGoal one(@PathVariable Long id) { return repo.findById(id).orElse(null); }
    @PostMapping public SavingsGoal create(@RequestBody SavingsGoal g) {
        if (g.getSavedAmount() == null) g.setSavedAmount(0.0);
        return repo.save(g);
    }
    @PutMapping("/{id}")
    public SavingsGoal update(@PathVariable Long id, @RequestBody SavingsGoal g) {
        g.setId(id); return repo.save(g);
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "Deleted"; }
 
    @GetMapping("/{id}/plan")
    public Map<String,Object> plan(@PathVariable Long id) {
        SavingsGoal g = repo.findById(id).orElseThrow();
        long months = Math.max(1, ChronoUnit.MONTHS.between(LocalDate.now(), g.getTargetDate()));
        double remaining = g.getTargetAmount() - g.getSavedAmount();
        double perMonth = remaining / months;
        Map<String,Object> m = new HashMap<>();
        m.put("goal", g.getGoalName());
        m.put("monthsLeft", months);
        m.put("requiredMonthlySaving", Math.round(perMonth));
        m.put("expectedCompletion", g.getTargetDate());
        return m;
    }
}
