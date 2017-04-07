package search.android.tools;

import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import search.android.vo.SummaryPage;

/**
 * Created by nhnent on 2017. 4. 5..
 */

public class WikiPageFinder {
    private static String htmlUrl = "https://en.wikipedia.org/api/rest_v1/page/html/";
    private static String summaryUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/";
    private static String relatedUrl = "https://en.wikipedia.org/api/rest_v1/page/related/";

    private static Object getWikiData(String url, JsonParsing parser) {

        URL wikiUrl = null;
        HttpsURLConnection wikiConnection = null;
        InputStreamReader responseBodyReader = null;
        JsonReader jsonReader = null;
        Object resultParsing = null;

        try {
            wikiUrl = new URL(url); //MalformedException
            wikiConnection = (HttpsURLConnection) wikiUrl.openConnection(); //IOException
            wikiConnection.setRequestProperty("Accept", "application/problem+json");

            if(wikiConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                responseBodyReader = new InputStreamReader(wikiConnection.getInputStream(), "UTF-8");
                jsonReader = new JsonReader(responseBodyReader);

                resultParsing = parser.excuteParsing(jsonReader);

            }

        } catch (MalformedURLException e) {
            Log.d("Exception","잘못된 URL 요청입니다.");
            //throw new RuntimeException(e);
        } catch (IOException e) {
            Log.d("Error", "Connection을 열 수 없습니다.");
            //throw new RuntimeException(e);
        } finally {
            if (jsonReader != null) {
                try { jsonReader.close(); } catch (IOException e) {}
            }

            if(responseBodyReader != null) {
                try { responseBodyReader.close(); } catch (IOException e) { }
            }

            if (wikiConnection != null) {
                wikiConnection.disconnect();
            }
        }
        return resultParsing;
    }

    private static Object summaryJsonParsing(JsonReader jsonReader) {
        SummaryPage summaryPage = new SummaryPage();
        URL url = null;
        HttpURLConnection imgConnection = null;

        try {
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                String key = jsonReader.nextName();

                switch(key) {
                    case "title" :
                        summaryPage.setTitle(jsonReader.nextString().replace("_", " "));
                        break;
                    case "extract" :
                        summaryPage.setSummary(jsonReader.nextString().replace("_", " "));
                        break;
                    case "thumbnail" :
                        jsonReader.beginObject();
                        while(jsonReader.hasNext()) {
                            String thumbnailKey = jsonReader.nextName();
                            switch(thumbnailKey) {
                                case "source" :
                                    try {
                                        url = new URL(jsonReader.nextString());
                                        imgConnection = (HttpURLConnection) url.openConnection();
                                        summaryPage.setThumbnail(BitmapFactory.decodeStream(imgConnection.getInputStream()));
                                        imgConnection.disconnect();
                                    } catch(IOException e) {
                                    } finally {
                                        if(imgConnection != null) {
                                            imgConnection.disconnect();
                                        }
                                    }

                                    break;
                                default :
                                    jsonReader.skipValue();
                                    break;
                            }
                        }
                        jsonReader.endObject();
                        break;
                    default :
                        jsonReader.skipValue();
                        break;
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            summaryPage = null;
        }

        return summaryPage;
    }

    public static SummaryPage getSummaryPage(String title) {
        JsonParsing jsonParser = new JsonParsing() {
            @Override
            public Object excuteParsing(JsonReader jsonReader) throws IOException{
                return summaryJsonParsing(jsonReader);
            }
        };

        return (SummaryPage) getWikiData(summaryUrl + title, jsonParser);
    }

    public static List<SummaryPage> findRelatedPages(String title) {
        JsonParsing jsonParser = new JsonParsing() {
            @Override
            public Object excuteParsing(JsonReader jsonReader) throws IOException {

                List<SummaryPage> summaryPages = null;

                jsonReader.beginObject();
                while(jsonReader.hasNext()) {
                    String key = jsonReader.nextName();

                    switch(key) {
                        case "pages" :
                            jsonReader.beginArray();
                            summaryPages = new ArrayList<>();

                            while(jsonReader.hasNext()) {
                                SummaryPage summaryPage = (SummaryPage) summaryJsonParsing(jsonReader);
                                if(summaryPage != null) {
                                    summaryPages.add(summaryPage);
                                }
                            }

                            jsonReader.endArray();
                            break;

                        default :
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();

                return summaryPages;
            }
        };

        return (List<SummaryPage>) getWikiData(relatedUrl + title, jsonParser);
    }

    public static String getHtmlUrl(String title) {
        return htmlUrl + title;
    }

    private interface JsonParsing {
        Object excuteParsing(JsonReader jsonReader) throws IOException;
    }
}