package dev.alexandrevieira.sm.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.alexandrevieira.sm.domain.PositionTrade;
import dev.alexandrevieira.sm.services.PositionTradeService;

@RestController
@RequestMapping(value = "/api/position-trades")
public class PositionTradeResource {
	
	@Autowired
	PositionTradeService service;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<PositionTrade> find(@PathVariable Long id) {
		PositionTrade trade = service.find(id);
		
		return ResponseEntity.ok().body(trade);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/user")
	public ResponseEntity<List<PositionTrade>> findAllByUserEmail(@RequestParam(value = "user") String userEmail) {
		List<PositionTrade> trades = service.findAllByUserEmail(userEmail);
		
		return ResponseEntity.ok().body(trades);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<PositionTrade> insert(@RequestBody PositionTrade positionTrade) {
		positionTrade = service.insert(positionTrade);
		
		return ResponseEntity.ok().body(positionTrade);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/page")
	public ResponseEntity<Page<PositionTrade>> findPageByUserEmail(
			@RequestParam(value = "user") String userEmail,
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction, 
			@RequestParam(value = "orderBy", defaultValue = "date") String orderBy) {
		
		
		
		Page<PositionTrade> trades = service.findPageByUserEmail(userEmail, page, linesPerPage, direction, orderBy);
		return ResponseEntity.ok().body(trades);
	}
}
