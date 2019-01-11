package com.jscandic.uit.jscandictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TranslateActivity extends BaseActivity implements View.OnClickListener {
    private static final String API_KEY = "AIzaSyDR-IjtvUJUqmtenBaS7j5bqe22T6RplsE";


    @BindView(R.id.btn_clear_all)
    Button btnClearAll;
    @BindView(R.id.btn_translate)
    Button btnTranslate;
    @BindView(R.id.edt_text_to_translate)
    EditText edtTextToTrans;
    @BindView(R.id.edt_result)
    EditText edtResult;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_translate);
        ButterKnife.bind(this);

        CustomActionBarFragment actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.SIMPLE_TITLE_TYPE,
                getString(R.string.multi_translate_title));

        String ocrWord = getIntent().getStringExtra(OCRActivity.OCR_WORD_RESULT);
        if (ocrWord != null){
            edtTextToTrans.setText(ocrWord);
        }

        btnClearAll.setOnClickListener(this);
        btnTranslate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_clear_all:
                edtTextToTrans.setText("");
                edtResult.setText("");
                break;

            case R.id.btn_translate:
                btnTranslateClick();
                break;
        }
    }

    private void btnTranslateClick(){
        //hide soft-keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtTextToTrans.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        final String inputText = edtTextToTrans.getText().toString();
        final Handler textViewHandler = new Handler();

        class GoogleTranslator extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                TranslateOptions options = TranslateOptions.newBuilder()
                        .setApiKey(API_KEY)
                        .build();
                Translate translate = options.getService();
                final Translation translation =
                        translate.translate(inputText,
                                Translate.TranslateOption.sourceLanguage("ja"),
                                Translate.TranslateOption.targetLanguage("vi"));
                textViewHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (edtResult != null) {
                            edtResult.setText(translation.getTranslatedText());
                        }
                    }
                });
                return null;
            }
        }
        new GoogleTranslator().execute();
    }
}