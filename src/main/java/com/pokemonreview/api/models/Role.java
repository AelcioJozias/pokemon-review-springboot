package com.pokemonreview.api.models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

}
