package com.pokemonreview.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.PokemonController;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@WebMvcTest(controllers = PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init() {
        pokemon = Pokemon.builder().id(1).name("pikachu").type("electric").build();
        review = Review.builder().title("title").content("content").stars(5).pokemon(pokemon).build();
        pokemon.setReviews(Collections.singletonList(review));
        reviewDto = ReviewDto.builder().title("review title").content("test content").stars(5).build();
        pokemonDto = PokemonDto.builder().id(1).name("pickachu").type("electric").build();
    }

    @Test
    public void PokemonController_CreatePokemon_ReturnCreated() throws Exception {
//        when(pokemonService.createPokemon(any())).thenReturn(pokemonDto);
        given(pokemonService.createPokemon(any(PokemonDto.class))).willAnswer((invocation -> invocation.getArgument(0)));
        ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(pokemonDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is(pokemonDto.getType())));
    }

    @Test
    public void PokemonController_getPokemons_ReturnPokemonResponse() throws Exception {
        String pageNumber = "1";
        String pageSize = "10";

        PokemonResponse pokemonResponse = PokemonResponse.builder()
                .pageNo(Integer.parseInt(pageNumber))
                .pageSize(Integer.parseInt(pageSize))
                .totalElements(1)
                .totalPages(1)
                .content(Collections.singletonList(pokemonDto))
                .build();

        given(pokemonService.getAllPokemon(anyInt(), anyInt())).willAnswer(invocation -> pokemonResponse);
        ResultActions response = mockMvc.perform(get("/api/pokemon")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page", pageNumber)
                .queryParam("pageSize", pageSize)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()",
                        CoreMatchers.is(pokemonResponse.getContent().size())))

        ;
        // example of how see the complete response
        System.out.println(response.andReturn().getResponse().getContentAsString());
    }


    @Test
    public void Given_APokemonId_When_FindThePokemonById_Then_ShouldReturnAPokemonDTO() throws Exception {
        when(pokemonService.getPokemonById(anyInt())).thenReturn(pokemonDto);
        ResultActions response = mockMvc.perform(get("/api/pokemon/{id}", 1).contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(pokemonDto.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(pokemonDto.getName())));
    }

    @Test
    public void Given_APokemon_When_UpdateThePokemon_Then_ShouldReturnThePokemonUpdated() throws Exception{
        given(pokemonService.updatePokemon(ArgumentMatchers.any(PokemonDto.class), ArgumentMatchers.anyInt())).willAnswer(invocation -> pokemonDto);

        ResultActions response = mockMvc.perform(put("/api/pokemon/1/update")
                .content(objectMapper.writeValueAsString(pokemonDto))
                .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(pokemonDto.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.equalTo(pokemonDto.getName())));
    }

    @Test
    public void Given_AnExistingPokemon_When_DeleteThePokemon_Then_ReturnStatus200() throws Exception {
        Mockito.doNothing().when(pokemonService).deletePokemonId(ArgumentMatchers.anyInt());

        ResultActions response = mockMvc.perform(delete("/api/pokemon/1/delete")
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }


}
