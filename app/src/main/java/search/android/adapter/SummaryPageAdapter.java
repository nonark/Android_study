package search.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import search.android.aos_search.R;
import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class SummaryPageAdapter extends RecyclerView.Adapter<SummaryPageAdapter.ViewHolder> {

    private List<SummaryPage> wikiPages;
    private int itemLayout;

    //Getter and Setter
    public List<SummaryPage> getWikiPages() {
        return wikiPages;
    }

    public void setWikiPages(List<SummaryPage> wikiPages) {
        this.wikiPages = wikiPages;
    }

    public int getItemLayout() {
        return itemLayout;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    public SummaryPageAdapter(List<SummaryPage> wikiPages, int itemLayout) {
        this.wikiPages = wikiPages;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SummaryPage wikiPage = wikiPages.get(position);
        holder.title.setText(wikiPage.getTitle());
        holder.summary.setText(wikiPage.getSummary());
    }

    @Override
    public int getItemCount() {
        return wikiPages.size();
    }

    /*
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView title;
        public TextView summary;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            summary = (TextView) itemView.findViewById(R.id.summary);
        }
    }
}
