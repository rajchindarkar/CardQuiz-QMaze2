package com.dc.msu.maze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dc.msu.maze.Adapters.QuestionAdapter;
import com.dc.msu.maze.Helpers.OptionSelectedListener;
import com.dc.msu.maze.Helpers.Views.NonSwipeableViewPager;
import com.dc.msu.maze.Models.QuestionModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    List<QuestionModel> list = new ArrayList<>();

    HashMap<Integer, List<String>> answers = new HashMap<>();

    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        nextBtn = findViewById(R.id.finishBtn);
        NonSwipeableViewPager mPager = findViewById(R.id.viewPager);
        // here we are putting questions and answers in list to show in viewpager
        list.add(new QuestionModel(1,"Amphibians, reptiles, birds, and mammals are all examples of what?", "flying animals,vertebrates,invertebrates", "1"));
        list.add(new QuestionModel(2,"The nervous system includes five senses. Which is NOT one of the five senses?", "sight,hearing,taste,none of the above", "3"));
        list.add(new QuestionModel(3,"This is the largest animal group.", "invertebrates,vertebrates,cold-blooded animals,warm-blooded animals", "0"));
        list.add(new QuestionModel(4,"Which two body systems work together to move the body's bones?", "nervous and skeletal,skeletal and muscular,circulatory and digestive,muscular and nervous", "1"));
        list.add(new QuestionModel(5,"Which of the following has opposable thumbs. select all that apply", "koala,goat,gorillas,birds", "0,2"));
        // here we are setting pagerAdapter with viewPager
        PagerAdapter mAdapter = new QuestionAdapter(list, (id, strings) -> answers.put(id, strings));
        mPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        nextBtn.setOnClickListener(view -> {
            int current = mPager.getCurrentItem();
            QuestionModel model = list.get(current);
            List<String> strings = answers.get(model.getQuestionId());
            // here we are checking that user is selected any answer or not
            if (strings == null || strings.size() == 0) {
                CustomToast.toastShort(view.getContext(), "Please select at least one option");
                return;
            }

            if (mPager.getCurrentItem() == (list.size() - 1)) {
                int result = 0;
                // here we are checking user answer is correct or not
                for (QuestionModel mod : list) {
                    List<String> answersList = answers.get(mod.getQuestionId());
                    if (BaseUtils.equalLists(mod.getAnswers(), answersList)) {
                        // if user answer is correct
                        result++;
                    }
                }
                //  here we are storing questions results in Firebase Database
                HashMap<String, Object> data = new HashMap<>();
                data.put("result", result);
                data.put("total", list.size());
                data.put("date", String.valueOf(System.currentTimeMillis()));
                FirebaseDatabase.getInstance().getReference().child("users").child(BaseUtils.mineId).child("history").push().setValue(data);
                startActivity(new Intent(this, ResultActivity.class).putExtra("result", result).putExtra("total", list.size()));
                finish();
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
            }
        });

    }
}