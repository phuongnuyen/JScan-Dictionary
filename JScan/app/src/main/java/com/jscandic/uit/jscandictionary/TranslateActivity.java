package com.jscandic.uit.jscandictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TranslateActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.btn_clear_all)
    Button btnClearAll;
    @BindView(R.id.btn_translate)
    Button btnTranslate;
    @BindView(R.id.edt_text_to_translate)
    EditText edtTextToTrans;
    @BindView(R.id.edt_result)
    EditText edtResult;

    String txtInput;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_translate);
        ButterKnife.bind(this);

        CustomActionBarFragment actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.SIMPLE_TITLE_TYPE,
                getString(R.string.multi_translate_title));

//        String ocrWord = getIntent().getStringExtra(OCRActivity.OCR_WORD_RESULT);
//        if (ocrWord != null){
//            edtTextToTrans.setText(ocrWord);
//        }

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


        txtInput = edtTextToTrans.getText().toString();
        class bgStuff extends AsyncTask<Void, Void, Void> {
            String translatedText = "";

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    translatedText = translate(txtInput);
                } catch (Exception e) {
                    e.printStackTrace();
                    translatedText = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                edtResult.setText(translatedText);
                super.onPostExecute(result);
            }
        }
        edtResult.setText("");
        new bgStuff().execute();
    }

    public String translate(String text) throws Exception{
        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("FAPP_AN");
        Translate.setClientSecret("Oi5YL6qmXAVVxMvJt/rWqlYdTjKW71LDFz5bMFlC370=");
        String translatedText = "";
        translatedText = Translate.execute(text, Language.JAPANESE);
        return translatedText;
    }
}