package dev.alexandrevieira.sm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.alexandrevieira.sm.domain.PositionTrade;

@Repository
public interface PositionTradeRepository extends JpaRepository<PositionTrade, Long> {
	public List<PositionTrade> findAllByUserEmailOrderByDateAscTypeAsc(String userEmail);
	
	public List<PositionTrade> findAllByUserEmailAndStockTickerOrderByDateAscTypeAsc(String userEmail, String stockTicker);
	
	public Page<PositionTrade> findAllByUserEmail(String userEmail, Pageable pageable);
}
