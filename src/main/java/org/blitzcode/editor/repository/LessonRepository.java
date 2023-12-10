package org.blitzcode.editor.repository;
import org.blitzcode.editor.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findLessonByName(String name);
}