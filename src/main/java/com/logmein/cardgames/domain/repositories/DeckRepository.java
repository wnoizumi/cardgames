package com.logmein.cardgames.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logmein.cardgames.domain.entities.Deck;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

	@Query("select d from Deck d left join fetch d.cards c left join fetch d.game g where d.uuid = :uuid")
	Optional<Deck> findOneWithRelationsByUuid(@Param("uuid") UUID uuid);

	@Query("select d from Deck d left join fetch d.game g where d.uuid = :uuid")
	Optional<Deck> findOneByWithGameUuid(UUID uuid);
}
