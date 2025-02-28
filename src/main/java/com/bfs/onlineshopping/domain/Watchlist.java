package com.bfs.onlineshopping.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "watchlist")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(WatchlistId.class)
public class Watchlist {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
