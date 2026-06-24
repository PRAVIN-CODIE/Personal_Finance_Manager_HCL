package com.finance.controller;
 
import com.finance.model.Transaction;
import com.finance.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
 
@RestController
@RequestMapping("/api/ai")
public class AIController {
 
    @Autowired private TransactionRepo repo;
 
    // 1. Spending habit analysis
    @GetMapping("/analysis")
    public List<String> analysis() {
        List<Transaction> txs = repo.findAll().stream()
                .filter(t -> "EXPENSE".equals(t.getType())).toList();
        List<String> insights = new ArrayList<>();
        if (txs.isEmpty()) { insights.add("Not enough data for analysis."); return insights; }
 
        double weekend = txs.stream().filter(t -> {
            DayOfWeek d = t.getDate().getDayOfWeek();
            return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
        }).mapToDouble(Transaction::getAmount).sum();
        double weekday = txs.stream().mapToDouble(Transaction::getAmount).sum() - weekend;
        if (weekday > 0) {
            double pct = ((weekend / 2.0) / (weekday / 5.0) - 1) * 100;
            if (pct > 10)
                insights.add("You spend " + Math.round(pct) + "% more per day on weekends.");
        }
 
        Map<String, Double> byCat = txs.stream().collect(
                Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
        byCat.entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(e ->
            insights.add("Top spending category: " + e.getKey() + " (₹" + Math.round(e.getValue()) + ")"));
 
        return insights;
    }
 
    // 2. Next month prediction (3-month moving average)
    @GetMapping("/predict")
    public Map<String, Double> predict() {
        Map<YearMonth, Map<String, Double>> grouped = new HashMap<>();
        for (Transaction t : repo.findAll()) {
            if (!"EXPENSE".equals(t.getType())) continue;
            YearMonth ym = YearMonth.from(t.getDate());
            grouped.computeIfAbsent(ym, k -> new HashMap<>())
                   .merge(t.getCategory(), t.getAmount(), Double::sum);
        }
        Map<String, List<Double>> catHistory = new HashMap<>();
        grouped.values().forEach(m -> m.forEach((k,v) ->
            catHistory.computeIfAbsent(k, x -> new ArrayList<>()).add(v)));
 
        Map<String, Double> prediction = new HashMap<>();
        catHistory.forEach((cat, list) -> {
            double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            prediction.put(cat, Math.round(avg * 100.0) / 100.0);
        });
        return prediction;
    }
 
    // 3. Savings suggestion
    @GetMapping("/suggestions")
    public List<String> suggestions() {
        List<String> out = new ArrayList<>();
        Map<String, Double> byCat = repo.findAll().stream()
            .filter(t -> "EXPENSE".equals(t.getType()))
            .collect(Collectors.groupingBy(Transaction::getCategory,
                    Collectors.summingDouble(Transaction::getAmount)));
        byCat.forEach((cat, amt) -> {
            if (amt > 1000) {
                double save = amt * 0.2;
                out.add("Reducing " + cat + " by 20% can save ₹" + Math.round(save) + "/month.");
            }
        });
        if (out.isEmpty()) out.add("Spending looks healthy. Keep tracking!");
        return out;
    }
 
