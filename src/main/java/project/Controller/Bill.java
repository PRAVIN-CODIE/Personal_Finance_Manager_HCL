package com.finance.controller;
 
import com.finance.model.Bill;
import com.finance.repo.BillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
 
@RestController
@RequestMapping("/api/bills")
public class BillController {
    @Autowired private BillRepo repo;
 
    @GetMapping public List<Bill> all() { return repo.findAll(); }
    @GetMapping("/{id}") public Bill one(@PathVariable Long id) { return repo.findById(id).orElse(null); }
    @PostMapping public Bill create(@RequestBody Bill b) {
        if (b.getPaid() == null) b.setPaid(false);
        return repo.save(b);
    }
    @PutMapping("/{id}")
    public Bill update(@PathVariable Long id, @RequestBody Bill b) { b.setId(id); return repo.save(b); }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { repo.deleteById(id); return "Deleted"; }
 
    @GetMapping("/upcoming")
    public List<Bill> upcoming() {
        LocalDate today = LocalDate.now();
        LocalDate in7 = today.plusDays(7);
        return repo.findAll().stream()
            .filter(b -> !b.getPaid() && !b.getDueDate().isBefore(today) && !b.getDueDate().isAfter(in7))
            .collect(Collectors.toList());
    }
}
