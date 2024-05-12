package com.pokemonreview.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private int id;
    private String title;
    private String content;
    private int stars;
}
