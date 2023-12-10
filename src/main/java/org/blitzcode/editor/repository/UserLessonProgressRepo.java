package org.blitzcode.editor.repository;

import org.blitzcode.editor.model.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLessonProgressRepo extends JpaRepository<UserLessonProgress, Long> {
}
