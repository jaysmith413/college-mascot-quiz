package dao;

import model.QuizQuestion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JdbcQuizQuestionDao implements QuizQuestionDao {

    JdbcTemplate jdbcTemplate;

    public JdbcQuizQuestionDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<QuizQuestion> getQuestionsForQuiz(String quizName) {
        String sql = "SELECT question.question_id, question_text, answer_text\n" +
                "FROM question\n" +
                "JOIN answer ON answer.answer_id = question.answer_id\n" +
                "ORDER BY question_id;";

        SqlRowSet questionRows = jdbcTemplate.queryForRowSet(sql);
        List<QuizQuestion> results = new ArrayList<>();
        while (questionRows.next()) {
            results.add(mapRowToQuizQuestion(questionRows));
        }
        return results;
    }

    public String[] getAnswersForQuestion(int questionId) {
        String sql = "SELECT answer_text\n" +
                "FROM answer\n" +
                "JOIN question ON answer.answer_id = question.answer_id\n" +
                "WHERE question_id = ?;";

        SqlRowSet answerRows = jdbcTemplate.queryForRowSet(sql, questionId);
        List<String> answers = new ArrayList<>();
        if (answerRows.next()) {
            answers.add(answerRows.getString("answer_text"));
        }
        Random rand = new Random();

        sql = "SELECT answer_text\n" +
                "FROM answer\n" +
                "WHERE answer_id = ?;";

        //add random answers from db
        int randomNum = rand.nextInt(42);
        while(randomNum == 0){
            randomNum = rand.nextInt(42);
        }
        List<Integer> usedNumbers = new ArrayList<>();

        for(int i = 0; i < 3; i++){

            answerRows = jdbcTemplate.queryForRowSet(sql, randomNum);
            if(answerRows.next()) {
                answers.add(answerRows.getString("answer_text"));
            }
            usedNumbers.add(randomNum);
            while(usedNumbers.contains(randomNum) || randomNum == 0){
                randomNum = rand.nextInt(42);
            }

        }



        return answers.toArray(new String[0]);
    }

    private QuizQuestion mapRowToQuizQuestion(SqlRowSet rowSet) {
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setQuestion(rowSet.getString("question_text"));
        quizQuestion.setCorrectAnswer(rowSet.getString("answer_text"));
        quizQuestion.setQuestionId(rowSet.getInt("question_id"));
        return quizQuestion;
    }
}
