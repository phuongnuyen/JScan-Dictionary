package com.jscandic.uit.jscandictionary.Quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.Database.DatabaseDescription;
import com.jscandic.uit.jscandictionary.R;

import java.util.ArrayList;
import java.util.Random;

public class ReadingQuizFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int LOADER = 0;

    @BindView(R.id.edt_answer)
    EditText edtAnswer;
    @BindView(R.id.ibtn_submit)
    ImageButton btnSubmit;
    @BindView(R.id.tv_english_meaning)
    TextView tvEnglishMeaning;
    @BindView(R.id.tv_vietnamese_meaning)
    TextView tvVietnameseMeaning;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.v_score_bg)
    View vScoreBg;
    @BindView(R.id.tv_question_number)
    TextView tvQuestionNumber;
    @BindView(R.id.ln_english_meaning)
    LinearLayout lnEnglishMeaning;
    @BindView(R.id.ln_vietnamese_meaning)
    LinearLayout lnVietnameseMeaning;

    private AlertDialog correctAnswerDialog;
    private Animation correctAnim;
    private Handler handler;
    private Random random;
    private ArrayList<String> listWord = new ArrayList<String>();
    private ArrayList<String> listEnglish = new ArrayList<String>();
    private ArrayList<String> listVietnamese = new ArrayList<String>();
    private int index;
    private int score;
    private int count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        super.onCreateView(inflater, viewGroup, bundle);
        View view = inflater.inflate(R.layout.fragment_reading_quiz, viewGroup, false);
        ButterKnife.bind(this, view);

        handler = new Handler();
        random = new Random();

        correctAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
        createCorrectAnswerDialog();
        edtAnswer.clearFocus();
        btnSubmit.setOnClickListener(onBtnSubmitClick);
        return view;
    }

    private View.OnClickListener onBtnSubmitClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((QuizActivity)getActivity()).hideSoftKeyboard(view);
            if(edtAnswer.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), getContext().getString(R.string.empty_word), Toast.LENGTH_SHORT).show();
                return;
            }

            if(edtAnswer.getText().toString().equals(listWord.get(index))){
                score++;
                vScoreBg.startAnimation(correctAnim);
                tvScore.postDelayed(new Runnable() {
                    @Override
                    public void run() {tvScore.setText(String.valueOf(score));
                    }
                },500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextQuestion();
                    }
                }, 2000);
            } else{
                correctAnswerDialog.setMessage(getContext().getString(R.string.correct_answer, listWord.get(index)));
                correctAnswerDialog.show();
            }
        }
    };

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            int wordIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_WORD);
            int englishIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_ENGLISH);
            int vietnameseIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_VIETNAMEE);

            do{
                listWord.add(data.getString(wordIndex));
                listEnglish.add(data.getString(englishIndex));
                listVietnamese.add(data.getString(vietnameseIndex));
            } while(data.moveToNext());

            reset();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursor = null;
        switch (id) {
            case LOADER:
                cursor = new CursorLoader(
                        getActivity(),
                        DatabaseDescription.Dictionary.CONTENT_URI,
                        null,
                        DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
                        new String[]{"1"},
                        null
                );
                break;
            default:
                break;
        }
        return cursor;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}


    private void reset(){
        index = random.nextInt(listWord.size());
        score = 0;
        count = 0;
        revise();
    }

    private void revise(){
        tvQuestionNumber.setText(getString(R.string.index_question, count + 1, listWord.size()));
        tvScore.setText(String.valueOf(score));
        edtAnswer.setText("");

        //set invisible all
        lnVietnameseMeaning.setVisibility(View.GONE);
        lnEnglishMeaning.setVisibility(View.GONE);

        if (!listEnglish.get(index).trim().isEmpty()) {
            tvEnglishMeaning.setText(listEnglish.get(index));
            lnEnglishMeaning.setVisibility(View.VISIBLE);
        }
        if (!listVietnamese.get(index).trim().isEmpty()) {
            tvVietnameseMeaning.setText(listVietnamese.get(index));
            lnVietnameseMeaning.setVisibility(View.VISIBLE);
        }
    }

    private void nextQuestion(){
        count++;
        index = random.nextInt(listWord.size());
        if(count < listWord.size())
            revise();
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String s = getString(R.string.revision_result, count,score / (double)count * 100);
            builder.setMessage(s);
            builder.setPositiveButton(R.string.revision_reset, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reset();
                }
            });
            builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            builder.show();
        }
    }

    private void createCorrectAnswerDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("qtry4wjts")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nextQuestion();
                        dialog.cancel();
                    }
                });
        correctAnswerDialog = builder.create();
    }
}
