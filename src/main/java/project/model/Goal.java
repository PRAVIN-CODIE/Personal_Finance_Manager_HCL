package com.finance.model;
 
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
 
@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class SavingsGoal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goalName;
    private Double targetAmount;
    private Double savedAmount;
    private LocalDate targetDate;
}
