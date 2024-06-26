package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByName(String name);

    Boolean existsByName(String name);
}
