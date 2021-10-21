package com.logmein.cardgames.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.logmein.cardgames.domain.entities.Player;
import com.logmein.cardgames.domain.entities.PlayingCard;
import com.logmein.cardgames.domain.projections.PlayingCardProjection;
import com.logmein.cardgames.domain.projections.SuitFaceProjection;

@Repository
public interface PlayingCardRepository extends JpaRepository<PlayingCard, Long> {

	@Query("SELECT pc FROM PlayingCard pc LEFT JOIN FETCH pc.game g LEFT JOIN FETCH pc.player p WHERE g.uuid = :gameuuid AND p IS NULL")
	List<PlayingCard> findAllAvailableByGame(@Param("gameuuid") UUID gameUuid);
	
	@Query("SELECT pc FROM PlayingCard pc LEFT JOIN FETCH pc.card c LEFT JOIN FETCH pc.game g LEFT JOIN FETCH pc.player p WHERE g.uuid = :gameuuid AND p IS NULL ORDER BY pc.shufflePosition DESC")
	List<PlayingCard> findNextDealByGame(@Param("gameuuid") UUID gameUuid, Pageable pageable);
	
	@Query("SELECT COUNT(pc) FROM PlayingCard pc LEFT JOIN pc.game g WHERE g.uuid = :gameuuid AND pc.player IS NULL")
	Integer countAvailableCardsByGame(@Param("gameuuid") UUID gameUuid);
	
	@Query("SELECT NEW com.logmein.cardgames.domain.projections.PlayingCardProjection(c.face, c.suit, pc.uuid, g.uuid, p.uuid) "
			+ "FROM PlayingCard pc JOIN pc.card c JOIN pc.game g JOIN pc.player p "
			+ "WHERE p.uuid = :playeruuid")
	List<PlayingCardProjection> findAllByPlayer(@Param("playeruuid") UUID playerUuid);
	
	@Query("SELECT pc FROM PlayingCard pc JOIN FETCH pc.player p JOIN FETCH pc.game g "
			+ "JOIN FETCH pc.card c WHERE g.uuid = :gameuuid")
	List<PlayingCard> findAllOnHandByGame(@Param("gameuuid") UUID gameUuid);

	@Query("SELECT NEW com.logmein.cardgames.domain.projections.SuitFaceProjection(c.face, c.suit, COUNT(c.id)) "
			+ "FROM PlayingCard pc JOIN pc.card c JOIN pc.game g "
			+ "WHERE g.uuid = :gameuuid " 
			+ "GROUP BY c.face, c.suit ")
	List<SuitFaceProjection> findAllSuitFaceSummariesByGame(@Param("gameuuid") UUID gameUuid);
}
