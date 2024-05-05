package com.pokemonreview.api.repository;


import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;


    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {

        //Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        //Act
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert

        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);

    }


    @Test
    public void PokemonRepository_GetAll_ReturnMoreThenOnePokemon() {

        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        Pokemon pokemon2 = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        pokemonRepository.saveAll(Arrays.asList(pokemon, pokemon2));

        // Act
        List<Pokemon> pokemonsList = pokemonRepository.findAll();


        // Assert
        Assertions.assertThat(pokemonsList).isNotNull();
        Assertions.assertThat(pokemonsList).size().isEqualTo(2);
    }

    @Test
    public void PokemonRepository_FindById_ReturnAPokemonAndHisCorrectAtributes() {

        // Arrange
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();


        Pokemon pokemonSaved = pokemonRepository.save(pokemon);

        // Act

        Pokemon pokemonReturned = pokemonRepository.findById(pokemon.getId()).orElseThrow();
        System.out.println(pokemonReturned.getId());

        // Assert
        Assertions.assertThat(pokemonReturned).isNotNull();
        Assertions.assertThat(pokemonReturned.getName()).isEqualTo("Pikachu");
        Assertions.assertThat(pokemonReturned.getType()).isEqualTo("Electric");
        Assertions.assertThat(pokemonReturned.getId()).isEqualTo(1);
    }

    @Test
    public void Given_PokemonWithAValidType_When_FindAPokemonByType_Then_ShouldReturnAPokemonWithTheTypeUsed() {
        Pokemon pokemon = Pokemon.builder()
                .name("Pikachu")
                .type("Electric")
                .build();

        Pokemon pokemonSaved = pokemonRepository.save(pokemon);
        pokemonRepository.save(pokemon);

        Pokemon pokemonReturned = pokemonRepository.findByType(pokemonSaved.getType()).orElseThrow();

        Assertions.assertThat(pokemonReturned).isNotNull();
        Assertions.assertThat(pokemonReturned.getType()).isEqualTo("Electric");
        Assertions.assertThat(pokemonReturned.getId()).isEqualTo(1);
    }
}
