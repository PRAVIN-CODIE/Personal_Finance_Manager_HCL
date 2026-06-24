package com.finance.model;
 
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
 
@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double amount;
    private String category;       // Food, Transport, Shopping, Bills, Entertainment, Income
    private String type;           // INCOME or EXPENSE
    private LocalDate date;
    private String note;
}
 
