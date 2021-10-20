package com.logmein.cardgames.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logmein.cardgames.domain.entities.PlayingCard;

@Repository
public interface PlayingCardRepository extends JpaRepository<PlayingCard, Long> {

	@Query("SELECT pc FROM PlayingCard pc LEFT JOIN FETCH pc.game g LEFT JOIN FETCH pc.player p WHERE g.uuid = :gameuuid AND p IS NULL")
	List<PlayingCard> findAllAvailableByGame(@Param("gameuuid") UUID gameUuid);
	
	@Query("SELECT pc FROM PlayingCard pc LEFT JOIN FETCH pc.card c LEFT JOIN FETCH pc.game g LEFT JOIN FETCH pc.player p WHERE g.uuid = :gameuuid AND p IS NULL ORDER BY pc.shufflePosition DESC")
	List<PlayingCard> findNextDealByGame(@Param("gameuuid") UUID gameUuid, Pageable pageable);
	
	@Query("SELECT COUNT(pc) FROM PlayingCard pc LEFT JOIN pc.game g WHERE g.uuid = :gameuuid AND pc.player IS NULL")
	Integer countAvailableCardsByGame(@Param("gameuuid") UUID gameUuid);
}
