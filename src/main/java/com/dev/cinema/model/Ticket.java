package com.dev.cinema.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "shopping_cart_id")
    private MovieSession movieSession;
    @OneToOne
    @MapsId
    @JoinColumn(name = "shopping_cart_id")
    private User user;
}
