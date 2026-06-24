package com.finance.model;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity 
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private Double monthlyLimit;
}
