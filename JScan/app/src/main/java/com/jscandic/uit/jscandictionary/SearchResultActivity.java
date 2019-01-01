package com.jscandic.uit.jscandictionary;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jscandic.uit.jscandictionary.Database.DatabaseDescription;

public class SearchResultActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface DictHomeListener {
        void onWordSelected(Uri contactUri);
    }

    private static final int DICT_LOADER_A = 0;
    private static final int DICT_LOADER_ALL = 1;

    @BindView(R.id.rcv_result)
    RecyclerView rcvResult;
    @BindView(R.id.tv_word_is_not_found)
    TextView tvNotFound;

    private CustomActionBarFragment actionBarFragment;
    private Uri uriSearch;
    private DictHomeListener listener;
    private DictionaryAdapter dictAdapter; // adapter for recyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        // join the ActionBar on the top of this layout
        actionBarFragment = (CustomActionBarFragment) getSupportFragmentManager().findFragmentById(R.id.fr_action_bar);
        actionBarFragment.setActionBarType(CustomActionBarFragment.SEARCH_RESULT_TITLE_TYPE, "SearchActivity");
        actionBarFragment.searchView.setOnQueryTextListener(svLookUpWordChangeListener);

        //Get word from MainActivity
        String wordQuery = getIntent().getStringExtra(MainActivity.WORD_QUERY);
        if (wordQuery != null){
            actionBarFragment.searchView.setQuery(wordQuery, true);
        }


        // recyclerView should display items in a vertical list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rcvResult.setLayoutManager(linearLayoutManager);
        // create recyclerView's adapter and item click listener
        dictAdapter = new DictionaryAdapter(
                new DictionaryAdapter.DictClickListener() {
                    @Override
                    public void onClick(Uri contactUri) {
//                        Intent intent = new Intent(getBaseContext(), DetailActivity.class);
//                        intent.putExtra(MainActivity.WORD_URI, contactUri);
//                        startActivity(intent);
                    }
                }
        );
        rcvResult.setAdapter(dictAdapter); // set the adapter
        // attach a custom ItemDecorator to draw dividers between list items
        rcvResult.addItemDecoration(new ItemDivider(this));
        // improves performance if RecyclerView's layout size never changes
        rcvResult.setHasFixedSize(true);
    }

    private SearchView.OnQueryTextListener svLookUpWordChangeListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            uriSearch = DatabaseDescription.Dictionary.buildContactUri(query);
            getSupportLoaderManager().restartLoader(DICT_LOADER_A, null, SearchResultActivity.this);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.length() == 0)
                getSupportLoaderManager().initLoader(DICT_LOADER_ALL, null, SearchResultActivity.this);
            else{
                uriSearch = DatabaseDescription.Dictionary.buildContactUri(newText);
                getSupportLoaderManager().restartLoader(DICT_LOADER_A, null, SearchResultActivity.this);
            }
            return false;
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // create an appropriate CursorLoader based on the id argument;
        // only one Loader in this fragment, so the switch is unnecessary
        switch (id) {
            case DICT_LOADER_A:
                return new CursorLoader(
                        this,
                        uriSearch, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        DatabaseDescription.Dictionary.COLUMN_WORD +
                                " COLLATE NOCASE ASC"); // sort order
            case DICT_LOADER_ALL:
                return new CursorLoader(
                        this,
                        DatabaseDescription.Dictionary.CONTENT_URI, // Uri of contacts table
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        DatabaseDescription.Dictionary.COLUMN_WORD +
                                " COLLATE NOCASE ASC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dictAdapter.swapCursor(data);
        if(data.getCount() == 0){
            String input = actionBarFragment.searchView.getQuery().toString();
            tvNotFound.setText(input + " "+ getString(R.string.not_found));
            tvNotFound.setVisibility(View.VISIBLE);
        }else{
            tvNotFound.setVisibility(View.GONE);
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dictAdapter.swapCursor(null);
    }
}
