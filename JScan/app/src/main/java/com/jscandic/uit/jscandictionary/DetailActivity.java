package com.jscandic.uit.jscandictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.Database.DatabaseDescription;
import com.jscandic.uit.jscandictionary.Database.DictContentProvider;

import java.util.Locale;

public class DetailActivity extends BaseActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, TextToSpeech.OnInitListener {

    private static final int WORD_LOADER = 0;

    @BindView(R.id.tv_synonymous)
    TextView tvSynonymous;
    @BindView(R.id.tv_english_meaning)
    TextView tvEnglishMeaning;
    @BindView(R.id.tv_vietnamese_meaning)
    TextView tvVietnameseMeaning;
    @BindView(R.id.tv_example)
    TextView tvExample;
    @BindView(R.id.ibtn_speaker)
    ImageButton btnSpeaker;
    @BindView(R.id.ln_synonymous)
    LinearLayout lnSynonymous;
    @BindView(R.id.ln_english_meaning)
    LinearLayout lnEnglishMeaning;
    @BindView(R.id.ln_vietnamese_meaning)
    LinearLayout lnVietnameseMeaning;
    @BindView(R.id.ln_example)
    LinearLayout lnExample;

    private CustomActionBarFragment actionBarFragment;
    private Uri wordUri;    // Uri of selected contact
    private boolean isLoaded = false;   // flag for cbFavorite check loading or user changing
    TextToSpeech    textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //get Uri from Main Activity
        wordUri = getIntent().getParcelableExtra(MainActivity.WORD_URI);
        getSupportLoaderManager().initLoader(WORD_LOADER, null, this);
        textToSpeech = new TextToSpeech(this, this);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.DETAIL_TITLE_TYPE, getString(R.string.detail_title));
        actionBarFragment.cbFavorite.setOnCheckedChangeListener(cbFavoriteCheckedChange);
        btnSpeaker.setOnClickListener(this);
    }

    private CompoundButton.OnCheckedChangeListener cbFavoriteCheckedChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isLoaded)
                return;

            ContentValues values = new ContentValues();
            values.put(DatabaseDescription.Dictionary.COLUMN_WORD, actionBarFragment.tvTitle.getText().toString());
            values.put(DatabaseDescription.Dictionary.COLUMN_SYNONYMOUS, tvSynonymous.getText().toString());
            values.put(DatabaseDescription.Dictionary.COLUMN_ENGLISH, tvEnglishMeaning.getText().toString());
            values.put(DatabaseDescription.Dictionary.COLUMN_VIETNAMEE, tvVietnameseMeaning.getText().toString());

            if (isChecked)
                values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, 1);
            else
                values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, 0);

            // update to databases
            int numRowUpdated = getApplicationContext().getContentResolver().update(
                    wordUri,
                    values,
                    null,
                    null
            );

            if(numRowUpdated > 0){
                String message = null;
                if(!isChecked)
                    message = getString(R.string.remove_favorite);
                else
                    message = getString(R.string.save_successfully);

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.save_unsuccessfully),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ibtn_speaker:
                if (!textToSpeech.isSpeaking() && actionBarFragment.tvTitle.getText() != null) {
                    textToSpeech.speak(
                            actionBarFragment.tvTitle.getText().toString(),
                            TextToSpeech.QUEUE_FLUSH,
                            null
                    );
                }
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        CursorLoader cursorLoader;
        DictContentProvider.isQueryingExactly = true;
        switch (id) {
            case WORD_LOADER:
                cursorLoader = new CursorLoader(
                        this,
                        wordUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }
        return cursorLoader;
    }


    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int wordIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_WORD);
            int synonymousIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_SYNONYMOUS);
            int englishIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_ENGLISH);
            int vietnameseIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_VIETNAMEE);
            int isFavoriteIndex = data.getColumnIndex(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE);


            // fill data
            actionBarFragment.setTitle(data.getString(wordIndex));
            setInvisibleAll();
            if (!data.getString(synonymousIndex).trim().isEmpty()) {
                tvSynonymous.setText(data.getString(synonymousIndex));
                lnSynonymous.setVisibility(View.VISIBLE);
            }
            if (!data.getString(englishIndex).trim().isEmpty()) {
                tvEnglishMeaning.setText(data.getString(englishIndex));
                lnEnglishMeaning.setVisibility(View.VISIBLE);
            }
            if (!data.getString(vietnameseIndex).trim().isEmpty()) {
                tvVietnameseMeaning.setText(data.getString(vietnameseIndex));
                lnVietnameseMeaning.setVisibility(View.VISIBLE);
            }

            if (data.getInt(isFavoriteIndex) == 1) {
                actionBarFragment.cbFavorite.setChecked(true);
            } else {
                actionBarFragment.cbFavorite.setChecked(false);
            }
            isLoaded = true;
        }
    }

    private void setInvisibleAll(){
        lnSynonymous.setVisibility(View.GONE);
        lnEnglishMeaning.setVisibility(View.GONE);
        lnVietnameseMeaning.setVisibility(View.GONE);
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {    }

    @Override
    public void onDestroy(){
        if(textToSpeech != null)
            textToSpeech.shutdown();
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR){
            textToSpeech.setLanguage(Locale.JAPAN);
        }
    }

}
