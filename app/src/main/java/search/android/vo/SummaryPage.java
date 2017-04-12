package search.android.vo;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class SummaryPage {
    private String thumbnail;
    private String title;
    private String summary;

    public SummaryPage() {
    }

    public SummaryPage(String thumbnail, String title, String summary) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.summary = summary;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
