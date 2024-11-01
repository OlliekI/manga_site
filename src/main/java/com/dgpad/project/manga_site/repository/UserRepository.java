package com.dgpad.project.manga_site.repository;

import com.dgpad.project.manga_site.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
