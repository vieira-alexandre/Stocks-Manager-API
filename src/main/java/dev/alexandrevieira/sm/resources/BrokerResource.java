package dev.alexandrevieira.sm.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.alexandrevieira.sm.domain.Broker;
import dev.alexandrevieira.sm.services.BrokerService;

@RestController
@RequestMapping(value = "/api/brokers")
public class BrokerResource {

	@Autowired
	private BrokerService brokerService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {
		List<Broker> broker = brokerService.findAll();
		return ResponseEntity.ok().body(broker);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> find(@PathVariable Long id) {
		Broker broker = brokerService.find(id);
		return ResponseEntity.ok().body(broker);
	}
}
