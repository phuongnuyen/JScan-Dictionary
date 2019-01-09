package com.jscandic.uit.jscandictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.Utils.LanguageUtils;
import com.jscandic.uit.jscandictionary.Utils.ThemeUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private static final String LANGUAGE = "language";
    private static final String THEME = "theme";


    @BindView(R.id.ibtn_theme_more)
    ImageButton btnThemeMore;
    @BindView(R.id.ibtn_language_more)
    ImageButton btnLanguageMore;
    @BindView(R.id.rl_theme_options)
    RelativeLayout rlThemeOptions;
    @BindView(R.id.rl_language_options)
    RelativeLayout rlLanguageOptions;
    @BindView(R.id.v_current_theme)
    View vCurrentTheme;
    @BindView(R.id.iv_current_language)
    ImageView vCurrentLanguage;
    @BindView(R.id.v_theme_green)
    View btnThemeGreen;
    @BindView(R.id.v_theme_blue)
    View btnThemeBlue;
    @BindView(R.id.v_theme_red)
    View btnThemeRed;
    @BindView(R.id.ibtn_language_us)
    ImageButton btnLanguageUS;
    @BindView(R.id.ibtn_language_vn)
    ImageButton btnLanguageVN;

    private boolean isOpenThemeOption = false;
    private boolean isOpenLanguageOption = false;
    private CustomActionBarFragment actionBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        vCurrentLanguage.setImageResource(LanguageUtils.flagIconResource);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.SIMPLE_TITLE_TYPE, getString(R.string.setting_title));
        actionBarFragment.btnBack.setOnClickListener(this);

        btnThemeMore.setOnClickListener(this);
        btnLanguageMore.setOnClickListener(this);
        btnThemeGreen.setOnClickListener(this);
        btnThemeBlue.setOnClickListener(this);
        btnThemeRed.setOnClickListener(this);
        btnLanguageUS.setOnClickListener(this);
        btnLanguageVN.setOnClickListener(this);
    }


    private void closeThemeOption(){
        rlThemeOptions.setVisibility(View.GONE);
        btnThemeMore.setRotation(180);
        isOpenThemeOption = false;
    }

    private void openThemeOption(){
        rlThemeOptions.setVisibility(View.VISIBLE);
        btnThemeMore.setRotation(0);
        isOpenThemeOption = true;
    }

    private void closeLanguageOption(){
        rlLanguageOptions.setVisibility(View.GONE);
        btnLanguageMore.setRotation(180);
        isOpenLanguageOption = false;
    }

    private void openLanguageOption(){
        rlLanguageOptions.setVisibility(View.VISIBLE);
        btnLanguageMore.setRotation(0);
        isOpenLanguageOption = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_theme_more:
                if (!isOpenThemeOption) {
                    closeLanguageOption();
                    openThemeOption();
                } else{
                    closeThemeOption();
                }
                break;

            case R.id.ibtn_language_more:
                if (!isOpenLanguageOption){
                    closeThemeOption();
                    openLanguageOption();
                } else {
                    closeLanguageOption();
                }
                break;

            case R.id.v_theme_green:
                ThemeUtils.changeToTheme(this, ThemeUtils.THEME_GREEN);
                break;

            case R.id.v_theme_blue:
                ThemeUtils.changeToTheme(this, ThemeUtils.THEME_BLUE);
                break;

            case R.id.v_theme_red:
                ThemeUtils.changeToTheme(this, ThemeUtils.THEME_RED);
                break;

            case R.id.ibtn_language_us:
                LanguageUtils.setLanguage(this, LanguageUtils.ENGLISH_US);
                break;

            case R.id.ibtn_language_vn:
                LanguageUtils.setLanguage(this, LanguageUtils.VIETNAMESE);

                break;

            case R.id.ibtn_back:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
