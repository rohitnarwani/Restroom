package com.locatar.restroom.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.locatar.restroom.common.SecurityHelper;
import com.locatar.restroom.model.User;
import com.locatar.restroom.repositories.SequenceDao;
import com.locatar.restroom.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	public Iterable<User> contact() {
		return userRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
	public Optional<User> show(@PathVariable String id) {
		return userRepository.findById(id);
	}

	@Autowired
	private SequenceDao sequenceDao;

	@PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createUser(@RequestBody User user) throws Exception {
		user.setUserId(sequenceDao.getNextSequenceId("user"));
		if (user != null && user.getPassword() != null) {
			String generatedPassword = SecurityHelper.createSecurePassword(user.getPassword());
			user.setPassword(generatedPassword);
		}
		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
				.buildAndExpand(savedUser.getUserId()).toUri();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return ResponseEntity.created(location).build();

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> executeLogin(@RequestBody User user) throws Exception {
		String password = user.getPassword();
		String securePassword = null;
		if (password != null) {
			securePassword = SecurityHelper.createSecurePassword(password);
			user.setPassword(securePassword);
		}
		List<User> users = userRepository.findByUserIdAndPassword(user.getUserId(), user.getPassword());
		if (!CollectionUtils.isEmpty(users)) {
			User userFromDB = users.get(0);
			if (userFromDB != null && userFromDB.isActive()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
						.buildAndExpand(userFromDB.getUserId()).toUri();
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.setContentType(MediaType.APPLICATION_JSON);
				return ResponseEntity.created(location).build();
			}
		}
		return ResponseEntity.notFound().build();
	}

}
