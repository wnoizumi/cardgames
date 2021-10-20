package com.logmein.cardgames.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logmein.cardgames.domain.entities.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
