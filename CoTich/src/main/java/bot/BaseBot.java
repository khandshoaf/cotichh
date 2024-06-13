package bot;

import database.Jdbc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseBot {
    private final String baseUrl;
    private final int maxThreads;
    private final long restTime;
    public final ExecutorService executorService;
    final List<String> visitedUrls = new ArrayList<>();

    public BaseBot(String baseUrl, int maxThreads, long restTime) {
        this.baseUrl = baseUrl;
        this.maxThreads = Math.max(maxThreads, 5);
        this.restTime = restTime;
        this.executorService = Executors.newFixedThreadPool(this.maxThreads);
    }

    public void startCrawling() {
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    crawl(baseUrl);
                }
            });
        }
    }
    public Elements display(String url, String type) {
        Elements datas = null;
        try {
            Document doc = Jsoup.connect(url).get();
            datas = doc.select(type);
        } catch (IOException e) {
        }
        return datas;
    }

    protected void crawl(String url) {
        try {
            if (!visitedUrls.contains(url)) {
                Document doc = Jsoup.connect(url).get();
                visitedUrls.add(url);
                Elements links = doc.select(".menuTop a[href]");
                // Get all the links inside the menuTop class
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    if (!visitedUrls.contains(linkHref)) {
                        visitedUrls.add(linkHref);
                        // Upon there, iterate the web inside
                        crawlPage(linkHref);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lấy dữ liệu thất bại " + url + ": " + e.getMessage());
        }
    }

    protected void crawlPage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements pagings = doc.select(".paging a[href]");
            for (Element page : pagings) {
                String pageHref = page.attr("href");
                if (!visitedUrls.contains(pageHref) && !pageHref.endsWith("/1.html")) {
                    visitedUrls.add(pageHref);
                    crawlPage(pageHref);
                }
            }
        } catch (IOException e) {
            System.err.println("Lấy dữ liệu thất bại " + url + ": " + e.getMessage());
        }
    }

    public int getAllMangaInPage(String url) throws IOException, SQLException {
        Document document = Jsoup.connect(url).get();
        Elements elements = document.getElementsByClass(".news-item");
        for (int i = 0; i < elements.size(); i++) {
            Elements elm_link = elements.get(i).getElementsByTag("a");
            for (int j = 0; j < elm_link.size(); j++) {
                String link = elm_link.first().absUrl("href");
                String title = elm_link.first().attr("title");
                Jdbc jdbc = new Jdbc();
                jdbc.savePage(0,title,link);
            }
        }
        return 1;
    }
}

