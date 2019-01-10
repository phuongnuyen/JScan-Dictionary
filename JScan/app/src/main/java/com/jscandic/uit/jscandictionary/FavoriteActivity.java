package com.jscandic.uit.jscandictionary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.Database.DatabaseDescription;

public class FavoriteActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String WORD_URI = "word_uri";
    public static final int LOADER = 0;

    @BindView(R.id.tv_num_of_word)
    TextView tvNumOfWord;
    @BindView(R.id.rcv_favorite_list)
    RecyclerView rcvFavorite;
    @BindView(R.id.tv_notify_empty_data)
    TextView tvEmptyData;

    private DictionaryAdapter adapter;
    private CustomActionBarFragment actionBarFragment;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        adapter = new DictionaryAdapter(new DictionaryAdapter.DictClickListener() {
            @Override
            public void onClick(Uri contactUri) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra(FavoriteActivity.WORD_URI, contactUri);
                startActivity(intent);
            }
        });

        //setting recycleview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rcvFavorite.setLayoutManager(linearLayoutManager);
        rcvFavorite.setHasFixedSize(true);
        rcvFavorite.addItemDecoration(new ItemDivider(this));
        rcvFavorite.setAdapter(adapter);

        //get favorite words from database
        getSupportLoaderManager().initLoader(LOADER, null, FavoriteActivity.this);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.FAVORITE_TITLE_TYPE, getString(R.string.favorite_title));
        actionBarFragment.btnBin.setOnClickListener(btnBinClickListener);
    }

    //delete all favorite words
    private View.OnClickListener btnBinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
            builder.setMessage(getString(R.string.confirm_delete_all));
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, "0");
                    getContentResolver().update(
                            DatabaseDescription.Dictionary.CONTENT_URI,
                            values,
                            DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
                            new String[]{"1"}
                    );
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case LOADER:
                return new CursorLoader(
                        this,
                        DatabaseDescription.Dictionary.CONTENT_URI,
                        null,
                        DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
                        new String[]{"1"},
                        null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        tvNumOfWord.setText(
                this.getString(R.string.num_of_words, data.getCount())
        );

        if (data.getCount() == 0){
            tvEmptyData.setVisibility(View.VISIBLE);
        } else {
            tvEmptyData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.menu_reading_revision:
//                listener.onReadingRevision();
//                return true;
//            case R.id.menu_listening_revision:
//                listener.onListeningRevision();
//                return true;
//            case R.id.menu_delete_all:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage(this.getString(R.string.confirm_delete_all));
//                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ContentValues values = new ContentValues();
//                        values.put(DatabaseDescription.Dictionary.COLUMN_ISFAVORITE, "0");
//                        getContentResolver().update(
//                                DatabaseDescription.Dictionary.CONTENT_URI,
//                                values,
//                                DatabaseDescription.Dictionary.COLUMN_ISFAVORITE + "=?",
//                                new String[]{"1"}
//                        );
//                    }
//                });
//                builder.setNegativeButton(android.R.string.cancel, null);
//                builder.show();
//
//                return true;
//            default:
//                return false;
//        }
//    }



}
