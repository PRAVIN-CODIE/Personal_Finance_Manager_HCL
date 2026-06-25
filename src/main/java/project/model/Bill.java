package com.finance.model;
 
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
 
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double amount;
    private LocalDate dueDate;
    private Boolean paid;
}
