package com.jscandic.uit.jscandictionary;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jscandic.uit.jscandictionary.Database.DatabaseDescription.Dictionary;

public class DictionaryAdapter
        extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    // interface implemented by ContactsFragment to respond
    // when the user touches an item in the RecyclerView
    public interface DictClickListener {
        void onClick(Uri contactUri);
    }

    // nested subclass of RecyclerView.ViewHolder used to implement
    // the view-holder pattern in the context of a RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        // configures a RecyclerView item's ViewHolder
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_word);

            // attach listener to itemView
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        // executes when the contact in this ViewHolder is clicked
                        @Override
                        public void onClick(View view) {
                            clickListener.onClick(Dictionary.buildContactUri(
                                    textView.getText().toString()
                            ));
                        }
                    }
            );
        }
    }

    // ContactsAdapter instance variables
    private Cursor cursor = null;
    private final DictClickListener clickListener;

    // constructor
    public DictionaryAdapter(DictClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // sets up new list item and its ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the android.R.layout.simple_list_item_1 layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.words_item, parent, false);
        return new ViewHolder(view); // return current item's ViewHolder
    }

    // sets the text of the list item to display the search tag
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.textView.setText(cursor.getString(cursor.getColumnIndex(
                Dictionary.COLUMN_WORD)));
    }

    // returns the number of items that adapter binds
    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    // swap this adapter's current Cursor for a new one
    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}