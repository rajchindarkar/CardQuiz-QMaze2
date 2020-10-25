package com.dc.msu.maze.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionModel implements Serializable {

    private int questionId;
    private String questionText;
    private List<String> options;
    private List<String> answers;

    public QuestionModel(int id, String questionText, String options, String answers) {
        this.questionId = id;
        this.questionText = questionText;
        this.options = Arrays.asList(options.split(","));
        this.answers = Arrays.asList(answers.split(","));
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
