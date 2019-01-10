package com.jscandic.uit.jscandictionary.Quiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.BaseActivity;
import com.jscandic.uit.jscandictionary.CustomActionBarFragment;
import com.jscandic.uit.jscandictionary.R;

public class QuizMainFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.ln_definition_quiz)
    LinearLayout btnDefinitionQuiz;
    @BindView(R.id.ln_synonymous_quiz)
    LinearLayout btnSynonymousQuiz;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_quiz_main, parent, false);
        ButterKnife.bind(this, view);

        btnDefinitionQuiz.setOnClickListener(this);
        btnSynonymousQuiz.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ln_definition_quiz:
                ((QuizActivity)getActivity()).replaceFragment(new ReadingQuizFragment());
                break;

            case R.id.ln_synonymous_quiz:
                ((QuizActivity)getActivity()).replaceFragment(new ListeningQuizFragment());
                break;
        }
    }
}
