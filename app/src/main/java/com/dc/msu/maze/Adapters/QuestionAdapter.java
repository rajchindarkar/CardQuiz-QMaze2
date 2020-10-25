package com.dc.msu.maze.Adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.dc.msu.maze.BaseUtils;
import com.dc.msu.maze.Helpers.OptionSelectedListener;
import com.dc.msu.maze.Models.QuestionModel;
import com.dc.msu.maze.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionAdapter extends PagerAdapter {

    private List<QuestionModel> questionModels;
    private OptionSelectedListener optionListener;

//    RadioButton firstAns;
//    RadioButton secondAns;
//    RadioButton thirdAns;
//    RadioButton forthAns;
//    RadioButton fifthAns;

    public QuestionAdapter(List<QuestionModel> models, OptionSelectedListener listener) {
        this.questionModels = models;
        this.optionListener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return questionModels.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, final int position) {
        final View layout = LayoutInflater.from(view.getContext()).inflate(R.layout.question_layout, view, false);

//        firstAns = layout.findViewById(R.id.firstAns);
//        secondAns = layout.findViewById(R.id.secondAns);
//        thirdAns = layout.findViewById(R.id.thirdAns);
//        forthAns = layout.findViewById(R.id.forthAns);
//        fifthAns = layout.findViewById(R.id.fifthAns);

        final QuestionModel model = questionModels.get(position);

        TextView questionText = layout.findViewById(R.id.questionText);
        questionText.setText(model.getQuestionText());

        TextView questionNo = layout.findViewById(R.id.questionNo);
        questionNo.setText((position + 1) + "/" + questionModels.size());

        RadioGroup radioGroup = layout.findViewById(R.id.radioGroup);

        if (model.getAnswers().size() > 1) {
            for (int i=0; i<model.getOptions().size(); i++) {
                CheckBox check = new CheckBox(view.getContext());
                check.setText(model.getOptions().get(i));
                check.setPadding(8, 0, 0, 0);
                check.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.btn_bg_type));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    check.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#6200ED")));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 70);
                params.setMargins(0, 0, 0, 20);
                radioGroup.addView(check, i, params);

                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        sendResultToActivity(radioGroup, model.getQuestionId());
                    }
                });
            }
        } else {
            for (int i=0; i<model.getOptions().size(); i++) {
                RadioButton button = new RadioButton(view.getContext());
                button.setText(model.getOptions().get(i));
                button.setPadding(8, 0, 0, 0);
                button.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.btn_bg_type));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#6200ED")));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 70);
                params.setMargins(0, 0, 0, 20);
                radioGroup.addView(button, i, params);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sendResultToActivity(radioGroup, model.getQuestionId());
            }
        });

        if (model.getOptions().size() != 0) {
//            firstAns.setText(model.getOptions().get(0));
//            secondAns.setText(model.getOptions().get(1));
//            thirdAns.setText(model.getOptions().get(2));
//            forthAns.setText(model.getOptions().get(3));
//            if (model.getOptions().size() == 5) {
//                fifthAns.setVisibility(View.VISIBLE);
//                fifthAns.setText(model.getOptions().get(4));
//            } else {
//                fifthAns.setVisibility(View.GONE);
//            }
        }

//        if (model.getSelectedAns() != -1) {
//            switch (model.getSelectedAns()) {
//                case 0:
//                    firstAns.setChecked(true);
//                    break;
//                case 1:
//                    secondAns.setChecked(true);
//                    break;
//                case 2:
//                    thirdAns.setChecked(true);
//                    break;
//                case 3:
//                    forthAns.setChecked(true);
//                    break;
//                case 4:
//                    fifthAns.setChecked(true);
//                    break;
//                default:
//                    break;
//            }
//        }

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                RadioButton radioButton = (RadioButton) questionLayout.findViewById(selectedId);
//
//                HashMap<String, Object> data = new HashMap<>();
//                data.put("text", radioButton.getText());
//
//                if (i == R.id.firstAns) {
//                    questionModels.get(position).setSelectedAns(0);
//                    data.put("score", 1);
//                } else if (i == R.id.secondAns) {
//                    questionModels.get(position).setSelectedAns(1);
//                    data.put("score", 2);
//                } else if (i == R.id.thirdAns) {
//                    questionModels.get(position).setSelectedAns(2);
//                    data.put("score", 3);
//                } else if (i == R.id.forthAns) {
//                    questionModels.get(position).setSelectedAns(3);
//                    data.put("score", 4);
//                } else if (i == R.id.fifthAns) {
//                    questionModels.get(position).setSelectedAns(4);
//                    data.put("score", 0);
//                }
//
//                FirebaseDatabase.getInstance().getReference().child("users").child(BaseUtils.mineId).child("answers").child(String.valueOf(position)).child("answer").setValue(data);
//            }
//        });

        view.addView(layout, 0);

        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) { }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void sendResultToActivity(RadioGroup radioGroup, int id) {
        List<String> answers = new ArrayList<>();

        for (int j=0; j<radioGroup.getChildCount(); j++) {
            if (radioGroup.getChildAt(j) instanceof RadioButton) {
                RadioButton button = (RadioButton) radioGroup.getChildAt(j);
                if (button.isChecked()) {
                    answers.add(String.valueOf(j));
                }
            }

            if (radioGroup.getChildAt(j) instanceof CheckBox) {
                CheckBox check = (CheckBox) radioGroup.getChildAt(j);
                if (check.isChecked()) {
                    answers.add(String.valueOf(j));
                }
            }
        }

        optionListener.onOptionSelected(id, answers);
    }
}
