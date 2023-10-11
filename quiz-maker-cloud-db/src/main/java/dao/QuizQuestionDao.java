package dao;

import model.QuizQuestion;

import java.util.List;

public interface QuizQuestionDao {


    List<QuizQuestion> getQuestionsForQuiz(String quizName);

}
