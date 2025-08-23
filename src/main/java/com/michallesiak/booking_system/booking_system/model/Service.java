package com.michallesiak.booking_system.booking_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service extends BaseEntity {
    @NotBlank
    @Column(unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @NotNull
    @DecimalMin("0.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Min(15)
    private Integer durationMinutes;
    @ManyToMany(mappedBy = "services")
    private Set<Specialist> specialists;

    private boolean active = true;
}


