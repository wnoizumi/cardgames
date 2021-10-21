package com.logmein.cardgames.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logmein.cardgames.domain.entities.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	Optional<Player> findOneByUuid(UUID uuid);
	
	@Query("select p from Player p left join fetch p.game g where p.uuid = :uuid")
	Optional<Player> findOneWithGameByUuid(@Param("uuid") UUID uuid);

	@Query("select p from Player p left join fetch p.game g where g.uuid = :gameuuid")
	List<Player> findAllByGame(@Param("gameuuid") UUID gameUuid);
}
