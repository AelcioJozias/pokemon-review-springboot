package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.exceptions.PokemonNotFoundException;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_CreatePokemon_ReturnsPokemonDto() {
        Pokemon pokemon = mock(Pokemon.class);

        PokemonDto pokemonDto = mock(PokemonDto.class);

        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto pokemonCreated = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(pokemonCreated).isNotNull();
    }

    @Test
    public void PokemonService_GetAllPokemon_ReturnPokemonResponseDto() {
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse pokemonResponse = pokemonService.getAllPokemon(1, 10);

        Assertions.assertThat(pokemonResponse).isNotNull();

    }

    @Test
    public void PokemonService_GetPokemonById_ReturnPokemonResponseDto() {
        Pokemon pokemon = mock(Pokemon.class);
        when(pokemonRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(pokemon));
        PokemonDto pokemonDto = pokemonService.getPokemonById(1);
        Assertions.assertThat(pokemonDto).isNotNull();
    }

    @Test
    public void PokemonService_GetPokemonById_ReturnPokemonNotFoundException() {
        Pokemon pokemon = mock(Pokemon.class);
        when(pokemonRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(PokemonNotFoundException.class, () -> pokemonService.getPokemonById(1));
    }







}
