package com.finance.controller;
 
import com.finance.model.Subscription;
import com.finance.repo.SubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
 
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    @Autowired private SubscriptionRepo repo;
 
    @GetMapping public List<Subscription> all() { return repo.findAll(); }
    @GetMapping("/{id}") public Subscription one(@PathVariable Long id) { return repo.findById(id).orElse(null); }
    @PostMapping public Subscription create(@RequestBody Subscription s) {
        if (s.getActive() == null) s.setActive(true);
        return repo.save(s);
    }
    @PutMapping("/{id}")
    public Subscription update(@PathVariable Long id, @RequestBody Subscription s) {
        s.setId(id); return repo.save(s);
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "Deleted"; }
 
    @GetMapping("/total")
    public Map<String,Double> total() {
        double total = repo.findAll().stream().filter(Subscription::getActive)
                .mapToDouble(Subscription::getMonthlyCost).sum();
        Map<String,Double> m = new HashMap<>();
        m.put("monthlyTotal", total);
        return m;
    }
}
