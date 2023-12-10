package org.blitzcode.editor.repository;

import org.blitzcode.editor.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}