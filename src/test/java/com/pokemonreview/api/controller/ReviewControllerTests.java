package com.pokemonreview.api.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.controllers.ReviewController;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.service.ReviewService;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTests {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  private ReviewDto reviewDto;

  private final int pokemonId = 1;
  private final int reviewId = 1;

  @MockBean
  private ReviewService reviewService;

  @BeforeEach
  public void before() {
    this.reviewDto = ReviewDto.builder()
        .id(reviewId)
        .title("Perfect")
        .content("this is the greatest pokemon i have seen in my life")
        .stars(5)
        .build();
  }

  @Test
  public void Given_APokemonIdAndAReviewDto_When_CreateAReview_Then_ShouldReturnAReviewDto() throws Exception {
    given(reviewService.createReview(anyInt(), any(ReviewDto.class))).willReturn(reviewDto);
    ResultActions response = mockMvc.perform(post("/api/pokemon/{pokemonId}/reviews", pokemonId)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(reviewDto))
    );
    response.andExpectAll((e) -> {
      response.andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(reviewDto.getId())));
    });
  }

  @Test
  public void Given_APokemonId_When_SearchAListOfReviewByPokemonId_Then_ShouldReturnAListOfReviewFromTheGivePokemonId() throws Exception {
    given(reviewService.getReviewsByPokemonId(anyInt())).willReturn(Collections.singletonList(reviewDto));
    var response = mockMvc.perform(get("/api/pokemon/{pokemonId}/reviews", pokemonId));
    response.andExpect(MockMvcResultMatchers.status().isOk())
    .andExpect(MockMvcResultMatchers.jsonPath("$.size()", equalTo(1)))
    .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", equalTo(reviewDto.getContent())));
  }

  @Test
  public void Given_APokemonIdAndAReviewId_When_SearchByTheTwoIds_Then_ShouldReturnAReviewDto() throws Exception {
    given(reviewService.getReviewById(anyInt(), anyInt())).willReturn(reviewDto);
    var response = mockMvc.perform(get("/api/pokemon/{pokemonId}/reviews/{id}", pokemonId, reviewId));
    response.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void Given_AnExistingReview_When_UpdateTheRegister_Then_ShouldReturnTheUpdatedReviewDto() throws Exception {
    given(reviewService.updateReview(anyInt(), anyInt(), any(ReviewDto.class))).willReturn(reviewDto);

    var response = mockMvc.perform(put("/api/pokemon/{pokemonId}/reviews/{id}", pokemonId, reviewId)
      .content(objectMapper.writeValueAsString(reviewDto))
      .contentType(MediaType.APPLICATION_JSON)
    );
    response.andExpect(MockMvcResultMatchers.status().isOk());
  }


}
