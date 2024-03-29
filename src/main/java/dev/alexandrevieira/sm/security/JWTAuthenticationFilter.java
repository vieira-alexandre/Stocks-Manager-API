package dev.alexandrevieira.sm.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.alexandrevieira.sm.domain.User;
import dev.alexandrevieira.sm.dto.CredentialsDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
												HttpServletResponse response) throws AuthenticationException {

		try {
			CredentialsDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredentialsDTO.class);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					creds.getEmail(),
					creds.getPassword(), 
					new ArrayList<>());

			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response, 
											FilterChain chain, 
											Authentication authResult) throws IOException, ServletException {

		User user = ((User) authResult.getPrincipal());
		String token = jwtUtil.generateToken(user);
		
		response.addHeader("Access-Control-Expose-Headers", "Authorization");
		response.addHeader("Authorization", token);
		response.addHeader("Content-Type", "application/json");
		
		response.setStatus(HttpStatus.OK.value());
	}
}
