package org.blitzcode.editor.repository;
import org.blitzcode.editor.model.Lesson;
import org.blitzcode.editor.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findLessonByName(String name);
    @Query("SELECT q FROM Question q WHERE q.lesson.id = :lessonId")
    List<Question> findQuestionsByLessonId(@Param("lessonId") Long lessonId);
}