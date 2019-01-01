package com.jscandic.uit.jscandictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.Utils.ThemeUtils;

public class CustomActionBarFragment extends Fragment implements View.OnClickListener{
    public static final int SEARCH_RESULT_TITLE_TYPE = 0;   // for: SearchResultActivity
    public static final int SIMPLE_TITLE_TYPE = 1;          // for: OCRActivity, TranslateActivity,...
    public static final int FAVORITE_TITLE_TYPE = 2;        // for: FavoriteActivity,...
    public static final int DETAIL_TITLE_TYPE = 3;          // for: DetailActivity
    public static final int INFOMATION_TITLE_TYPE = 4;      // for: InformationActivity

    @BindView(R.id.ibtn_back)
    ImageButton btnBack;
    @BindView(R.id.ibtn_bin)
    ImageButton btnBin;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cb_favorite)
    CheckBox cbFavorite;
    @BindView(R.id.ibtn_home)
    ImageButton btnHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_custom_action_bar, container, false);
        ButterKnife.bind(this, view);

        //custom searchview (cause I can change it in xml)
        //TextColor
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setTextColor(ThemeUtils.getThemeColor(getActivity(), android.R.attr.textColorSecondary));
        //HintTextColor
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(ThemeUtils.getThemeColor(getActivity(), android.R.attr.textColorHint));

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ibtn_back:
                getActivity().onBackPressed();
                break;

            case R.id.ibtn_home:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
    }

    private void setInvisibleAll(){
        btnHome.setVisibility(View.GONE);
        btnBin.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.INVISIBLE);
        cbFavorite.setVisibility(View.INVISIBLE);
    }

    public void setActionBarType(int type, String title){
        setInvisibleAll();
        switch (type){
            case SEARCH_RESULT_TITLE_TYPE:
                btnHome.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                break;

            case DETAIL_TITLE_TYPE:
                btnHome.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                cbFavorite.setVisibility(View.VISIBLE);
                break;

            case FAVORITE_TITLE_TYPE:
                btnHome.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
                btnBin.setVisibility(View.VISIBLE);
                break;

            case SIMPLE_TITLE_TYPE:
                btnHome.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
                break;

            case INFOMATION_TITLE_TYPE:
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
                break;
        }
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }
}
