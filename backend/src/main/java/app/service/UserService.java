package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.model.Game;
import app.model.User;
import app.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository users;

	public void save(User user) {
		users.save(user);
	}

	public Optional<User> findByMail(String mail) {
		return users.findByMail(mail);
	}

	public Optional<User> findByName(String mail) {
		return users.findByMail(mail);
	}

	public List<User> findAll() {
		return users.findAll();
	}

	public Optional<User> findById(long id) {
		Optional<User> findById = users.findById(id);
		return findById;
	}

	public boolean existMail(String name) {
		Optional<User> user = findByMail(name);
		return user.isPresent();
	}

	public List<Game> findGamesInCartByUserId(Long userId, Pageable pageable) {
		return users.findGamesInCartByUserId(userId, pageable);
	}

	public int countGamesInCartByUserId(Long userId) {
		return users.countGamesInCartByUserId(userId);
	}
}
