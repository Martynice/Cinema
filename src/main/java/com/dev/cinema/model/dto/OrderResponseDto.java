package com.dev.cinema.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private List<Long> ticketsIds;
    private Long userId;
    private LocalDateTime orderDate;
}
