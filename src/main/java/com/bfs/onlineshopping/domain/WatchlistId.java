package com.bfs.onlineshopping.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistId implements Serializable {
    private Long user;

    private Long product;
}
