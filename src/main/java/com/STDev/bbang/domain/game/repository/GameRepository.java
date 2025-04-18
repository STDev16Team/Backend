package com.STDev.bbang.domain.game.repository;

import com.STDev.bbang.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByGameIdAndMemberId(Long game, Long memberId);
}
