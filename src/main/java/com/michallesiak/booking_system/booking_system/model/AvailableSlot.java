package com.michallesiak.booking_system.booking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

@Entity
@Table(name = "available_slots", uniqueConstraints = @UniqueConstraint(columnNames = {"specialist_id", "start_time"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSlot extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialist_id", nullable = false)
    @NotNull
    private Specialist specialist;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private boolean booked = false;

    @Version
    private Long version;
}
