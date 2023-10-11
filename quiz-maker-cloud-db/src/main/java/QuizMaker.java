
import Exceptions.InvalidInput;
import connection.DbConnection;
import dao.JdbcQuizQuestionDao;
import dao.Leaderboard;
import dao.NeonDbLeaderboard;
import dao.Player;
import model.QuizQuestion;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class QuizMaker {

    private final Scanner userInput = new Scanner(System.in);
    private final File quizFileObject = new File("test_quiz.txt");
    private final Leaderboard leaderboard = new NeonDbLeaderboard();

    private final DbConnection connection = new DbConnection();

    private final JdbcQuizQuestionDao quizQuestionDao = new JdbcQuizQuestionDao(connection.getConnection());
    private final String quizName = "College Mascots Quiz";

    public static void main(String[] args) {
        QuizMaker quizMaker = new QuizMaker();
        quizMaker.run();
    }

    public void run() {

        List<QuizQuestion> quizQuestions = quizQuestionDao.getQuestionsForQuiz(quizName);

        System.out.println("Welcome to the College Mascot Quiz!");

        int numberCorrect = askQuizQuestions(quizQuestions);

        if( numberCorrect > 0){
            askAddLeaderboard(numberCorrect);
        }
    }

    private int askQuizQuestions(List<QuizQuestion> quizQuestions){
        int numberCorrect = 0;
        //randomize correct responses, which are always at index 0
        int[] choices = new int[]{0,1,2,3, 3,2,1,0, 2,3,1,0, 1,0,2,3, 0,3,2,1, 2,0,1,3, 3,2,0,1, 0,1,2,3, 1,0,2,3, 3,2,1,0,
                                  0,1,2,3, 1,2,0,3, 3,2,1,0, 0,1,2,3, 2,0,1,3, 2,3,0,1, 1,2,3,0, 0,1,2,3};
        int i = 0;

        for(QuizQuestion eachQuestion: quizQuestions){
            String[] answers = quizQuestionDao.getAnswersForQuestion(eachQuestion.getQuestionId());
            System.out.println(eachQuestion.getQuestion());

            String[] scrambledAnswers = new String[]{answers[choices[i]], answers[choices[i+1]],
                    answers[choices[i+2]], answers[choices[i+3]]};

            System.out.println("1) " + scrambledAnswers[0]);
            System.out.println("2) " + scrambledAnswers[1]);
            System.out.println("3) " + scrambledAnswers[2]);
            System.out.println("4) " + scrambledAnswers[3]);
            i = i + 4;

            String userAnswer = "";
            try {
                userAnswer = userInput.nextLine();

                if (!userAnswer.equals("1") && !userAnswer.equals("2") && !userAnswer.equals("3") && !userAnswer.equals("4")) {
                    throw new InvalidInput();
                }
            } catch (InvalidInput e){
                System.out.println(e.getMessage());
                userAnswer = userInput.nextLine();
            }

            if((Integer.parseInt(userAnswer) == 1 && scrambledAnswers[0].equals(answers[0])) ||
                    (Integer.parseInt(userAnswer) == 2 && scrambledAnswers[1].equals(answers[0])) ||
                    (Integer.parseInt(userAnswer) == 3 && scrambledAnswers[2].equals(answers[0])) ||
                    (Integer.parseInt(userAnswer) == 4 && scrambledAnswers[3].equals(answers[0]))) {
                    numberCorrect++;
                    System.out.println("Correct!");
            } else{
                System.out.println("Incorrect!");
            }
            System.out.println("Current Score: " + numberCorrect);
        }

        return numberCorrect;
    }

    private void displayLeaderboard(){
        List<Player> players = this.leaderboard.getLeaderboard();

        System.out.println("Leaderboard");
        System.out.println("----------------------");

        for(int i = 0; i < players.size(); i++){
            Player eachPlayer = players.get(i);

            System.out.println((i + 1) + ". " + eachPlayer.getName() + " - " + eachPlayer.getScore() + " - " + eachPlayer.getDate());
        }
    }


    private void askAddLeaderboard(int numberCorrect) {
        System.out.println("Add name to leaderboard? (y/n)");
        String userAnswer = userInput.nextLine();

        if(userAnswer.equalsIgnoreCase("y")) {
            System.out.println("What is your name?");
            String name = userInput.nextLine();
            leaderboard.addLeaderboard(new Player(name, numberCorrect, LocalDate.now()));

            displayLeaderboard();
        }
    }

}
