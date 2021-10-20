package com.logmein.cardgames.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logmein.cardgames.domain.entities.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

	public Optional<Game> findOneByUuid(UUID uuid);
}
