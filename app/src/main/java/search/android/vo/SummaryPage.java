package search.android.vo;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class SummaryPage {
    private String thumbnailUrl;
    private String title;
    private String summary;

    public SummaryPage() {
    }

    public SummaryPage(String thumbnailUrl, String title, String summary) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.summary = summary;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
