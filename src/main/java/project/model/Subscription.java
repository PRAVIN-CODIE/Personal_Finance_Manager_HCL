package com.finance.model;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity 
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double monthlyCost;
    private String renewalCycle;   // MONTHLY / YEARLY
    private Boolean active;
}
 
