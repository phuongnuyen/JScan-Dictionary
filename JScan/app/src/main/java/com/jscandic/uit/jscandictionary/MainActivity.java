package com.jscandic.uit.jscandictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.gelitenight.waveview.library.WaveView;
import com.jscandic.uit.jscandictionary.Helper.WaveHelper;
import com.jscandic.uit.jscandictionary.Quiz.QuizActivity;
import com.jscandic.uit.jscandictionary.Utils.ThemeUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String WORD_URI = "word_uri";
    public static final String WORD_QUERY = "word_query";
    private WaveHelper mWaveHelper;

    @BindView(R.id.wave_view)
    WaveView mWaveView;
    @BindView(R.id.ln_logo)
    LinearLayout lnLogo;
    @BindView(R.id.rl_main_btns)
    RelativeLayout rlMainBtns;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.btn_ocr)
    LinearLayout btnOCR;
    @BindView(R.id.btn_quiz)
    LinearLayout btnQuiz;
    @BindView(R.id.btn_multi_line_translate)
    LinearLayout btnMultiLineTranslate;
    @BindView(R.id.btn_favorite)
    LinearLayout btnFavorite;
//    @BindView(R.id.btn_info)
//    LinearLayout btnInfo;
//    @BindView(R.id.btn_setting)
//    LinearLayout btnSetting;

    //  bind below views to change color
    @BindView(R.id.ibtn_ocr)
    ImageButton ibtnOcr;
    @BindView(R.id.ibtn_multi_line_translate)
    ImageButton ibtnMultiTranslate;
    @BindView(R.id.ibtn_quiz)
    ImageButton ibtnQuiz;
    @BindView(R.id.ibtn_favorite)
    ImageButton ibtnFavorite;
//    @BindView(R.id.ibtn_setting)
//    ImageButton ibtnSetting;
//    @BindView(R.id.ibtn_info)
//    ImageButton ibtnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.initWaveUI();

        ThemeUtils.changeToTheme(this, ThemeUtils.THEME_RED);

        btnOCR.setOnClickListener(this);
        btnQuiz.setOnClickListener(this);
        btnMultiLineTranslate.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        searchView.setOnQueryTextListener(searchViewOnQueryTextListener);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btn_ocr:
                intent = new Intent(this, OCRActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_quiz:
                intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_multi_line_translate:
                intent = new Intent(this, TranslateActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_favorite:
                intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                break;
        }
    }

    private SearchView.OnQueryTextListener searchViewOnQueryTextListener
            = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (!query.trim().isEmpty()){
                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra(WORD_QUERY, query);
                startActivity(intent);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private void initWaveUI(){
        //get Color
        int waveBehindColor = ThemeUtils.getThemeColor(this, R.attr.colorPrimary);
        int waveFrontColor = ThemeUtils.getThemeColor(this, android.R.attr.colorBackground);

        mWaveHelper = new WaveHelper(mWaveView);
        mWaveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveView.setWaveColor(waveBehindColor, waveFrontColor);
        mWaveView.setAlpha(0.9f);
        mWaveHelper.start();


        final Animation lnLogoFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        lnLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                lnLogo.setVisibility(View.VISIBLE);
                lnLogo.startAnimation(lnLogoFadeIn);
            }
        }, 1800);
        rlMainBtns.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlMainBtns.setVisibility(View.VISIBLE);
                rlMainBtns.startAnimation(animFadeIn);
                searchView.setVisibility(View.VISIBLE);
                searchView.startAnimation(animFadeIn);
            }
        }, 2100);

    }
}
