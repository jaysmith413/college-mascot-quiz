package dao;

import java.time.LocalDate;

public class Player {
    private String name;
    private int score;
    private LocalDate date = LocalDate.now();

    public Player(String name, int score, LocalDate date){
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
