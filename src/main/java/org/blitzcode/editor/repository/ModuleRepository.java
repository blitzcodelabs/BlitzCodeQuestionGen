package org.blitzcode.editor.repository;

import org.blitzcode.editor.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findModuleByName(String name);
}