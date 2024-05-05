package com.pokemonreview.api.repository;


import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class LombokTests {

    @Test
    public void WhenBuildPokemon_MayHaveEmptyListOfReviews() {

        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        Assertions.assertThat(pokemon.getReviews()).isNotNull();
        Assertions.assertThat(pokemon.getReviews()).isEmpty();
    }
}
