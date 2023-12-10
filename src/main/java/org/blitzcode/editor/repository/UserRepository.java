package org.blitzcode.editor.repository;

import org.blitzcode.editor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String name);
    boolean existsByEmail(String email);
    User findById(String id);
}