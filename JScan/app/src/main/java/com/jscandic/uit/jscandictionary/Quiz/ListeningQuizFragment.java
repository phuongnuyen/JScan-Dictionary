package com.jscandic.uit.jscandictionary.Quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;
import java.util.Random;

public class ListeningQuizFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener{
    private static final int LOADER = 0;

    @BindView(R.id.iv_speaker)
    ImageView btnSpeaker;
    @BindView(R.id.edt_answer)
    EditText edtAnswer;
    @BindView(R.id.ibtn_submit)
    ImageButton btnSubmit;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_question_number)
    TextView tvQuestionNumber;
    @BindView(R.id.v_score_bg)
    View vScoreBg;

    private AlertDialog correctAnswerDialog;
    private Animation correctAnim;
    private Handler handler;
    private TextToSpeech tts;
    private ArrayList<String> listFavorite = new ArrayList<String>();
    private Random random;
    private int index;
    private int score;
    private int count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle){
        View view = inflater.inflate(R.layout.fragment_listening_quiz, viewGroup, false);
        ButterKnife.bind(this, view);

        handler = new Handler();
        random = new Random();
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.JAPANESE);
            }
        });
        correctAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
        createCorrectAnswerDialog();
        edtAnswer.clearFocus();

        btnSpeaker.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_speaker:
                if (!tts.isSpeaking()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(
                                listFavorite.get(index),
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                        );
                    }
                }
                break;

            case R.id.ibtn_submit:
                onBtnSubmitClick(view);
                break;
        }
    }

    private void onBtnSubmitClick(View view) {
        ((QuizActivity)getActivity()).hideSoftKeyboard(view);
        if(edtAnswer.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), getContext().getString(R.string.empty_word), Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtAnswer.getText().toString().equals(listFavorite.get(index))){
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
            correctAnswerDialog.setMessage(getContext().getString(R.string.correct_answer, listFavorite.get(index)));
            correctAnswerDialog.show();
        }
    }

    private void reset(){
        index = random.nextInt(listFavorite.size());
        score = 0;
        count = 0;
        revise();
    }

    private void nextQuestion(){
        count++;
        index = random.nextInt(listFavorite.size());
        if(count < listFavorite.size())
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

    private void revise(){
        tvQuestionNumber.setText(getString(R.string.index_question, count + 1, listFavorite.size()));
        tvScore.setText(String.valueOf(score));
        edtAnswer.setText("");

        if (!tts.isSpeaking())
            tts.speak(listFavorite.get(index), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id){
            case LOADER:
                cursorLoader = new CursorLoader(
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
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            int wordIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_WORD);
            listFavorite.clear();

            do{
                listFavorite.add(data.getString(wordIndex));
            } while (data.moveToNext());

            reset();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onDestroy(){
        if(tts != null)
            tts.shutdown();
        super.onDestroy();
    }

    private void createCorrectAnswerDialog() {
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
