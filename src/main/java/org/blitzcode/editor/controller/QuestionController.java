package org.blitzcode.editor.controller;

import org.blitzcode.editor.model.Lesson;
import org.blitzcode.editor.repository.LessonRepository;
import org.blitzcode.editor.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionController {

    @Autowired
    private LessonRepository lessonRepository;

    public List<Question> getQuestionsFromLessonID(Long id) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);
        if(lessonOptional.isEmpty()){
            throw new RuntimeException("Lesson not found!");
        }
        return lessonOptional.get().getQuestions();
    }
}
