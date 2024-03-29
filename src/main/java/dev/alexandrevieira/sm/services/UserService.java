package dev.alexandrevieira.sm.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.alexandrevieira.sm.domain.Stock;
import dev.alexandrevieira.sm.domain.User;
import dev.alexandrevieira.sm.dto.UserNewDTO;
import dev.alexandrevieira.sm.repositories.UserRepository;
import dev.alexandrevieira.sm.services.exceptions.AuthorizationException;
import dev.alexandrevieira.sm.services.exceptions.DuplicateEntryException;
import dev.alexandrevieira.sm.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private StockService stockService;
	
	
	public List<User> findaAll() {
		return repository.findAll();
	}
	
	public User find(Long id) {
		//User user = authService.authenticated();
		
		if(!authService.isAdmin() && !authService.isTheUser(id)) {
			throw new AuthorizationException("Acesso negado");
		}
		
		
		Optional<User> opt = repository.findById(id);
		
		
		
		//Não remover o lançamento de exceção. Os métodos update() e delete() fazem uso dele
		//Em caso de mudanças, reformular o tratamento de exceção
		return opt.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getSimpleName()));
	}
	
	public User findByEmail(String email) {
		
		if(!authService.isAdmin() && !authService.isTheUser(email)) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<User> opt = repository.findByEmailIgnoreCase(email);
		return opt.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Email: " + email + ", Tipo: " + User.class.getSimpleName()));
	}
	
	public User insert(User user) {
		Optional<User> opt = repository.findByEmailIgnoreCase(user.getEmail());
		
		if(opt.isPresent()) {
			throw new DuplicateEntryException(String.format("E-mail '%s' já cadastrado.", user.getEmail()));
		}
		
		user.setId(null);
		return repository.save(user);
	}
	
	public User update(User user) {
		//Chamando find, pois caso não exista o ele já lançará a exceção
		find(user.getId());
		return repository.save(user);
	}
	
	@Transactional
	public void delete(long id) {
		//Chamando find, pois caso não exista o ele já lançará a exceção
		find(id);
		repository.deleteById(id);
	}
	
	public Set<Stock> getFavorites(String email) {
		User user = this.findByEmail(email);
		
		//List<StockDTO> response = user.getFavoriteStocks().stream().map(x -> new StockDTO(x)).collect(Collectors.toList());
		Set<Stock> response = user.getFavoriteStocks();
		
		return response;
	}
	
	public void insertFavorite(String userEmail, String stockTicker) {
		User user = findByEmail(userEmail);
		Long id = user.getId();
		
		if(!authService.isAdmin() && !authService.isTheUser(id)) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Stock stock = stockService.find(stockTicker);
		
		user.getFavoriteStocks().add(stock);
		repository.save(user);
	}
	
	public void deleteFavorite(String userEmail, String stockTicker) {
		User user = findByEmail(userEmail);
		Long id = user.getId();
		
		if(!authService.isAdmin() && !authService.isTheUser(id)) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Stock stock = stockService.find(stockTicker);
		
		if(user.getFavoriteStocks().contains(stock)) {
			user.getFavoriteStocks().remove(stock);

			repository.save(user);
		}
		else {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Email: " + userEmail + ", Stock: " + stockTicker);
		}
		
	}
	
	public User fromDTO(UserNewDTO objDTO) {
		User user = new User(null, objDTO.getFirstName(), objDTO.getLastName(), objDTO.getEmail(), passwordEncoder.encode(objDTO.getPassword()));
		
		return user;
	}

	public Long getUserIdByEmail(String email) {
		Long userId = repository.getIdByEmail(email);
		
		if(userId == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Email: " + email + ", Tipo: " + User.class.getSimpleName());
		}
		
		return userId;
	}

	

	
}
