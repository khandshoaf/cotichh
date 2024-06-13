package bot;

import bot.obj.Truyen;
import database.Jdbc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class StoryCrawler extends BaseBot{
    public boolean isRunning = false;
    public StoryCrawler(String baseUrl) {
        super(baseUrl, 5, 1000);
        isRunning = true;
    }

    @Override
    protected void crawl(String baseUrl) {
        super.crawl(baseUrl);
        for (String url : visitedUrls) {
            processStoryData(url);
        }
    }

    protected void processStoryData(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String type = ".news-item"; // Loại truyện
            Elements data = doc.select(type);
            String tag = ".title > h1 > a";
            Elements tags = doc.select(tag);
            if (!tags.html().isEmpty()) {
                System.out.println("Tags: " + tags.html());
                String[] parts = url.split("/");
                String pagePart = parts[parts.length - 1];
                String pageNumber = pagePart.split("\\.")[0];
                if (!pageNumber.matches("\\d+")) pageNumber = "1";
                System.out.println("Trang: " + pageNumber);
            }

            for (Element item : data) {
                String title = item.select("h4 > a").html();
                String linkUrl = item.select("h4 > a").attr("href");
                String img = item.select(".img > a > img").attr("src");
                String info = item.select(".info-post").text();
                String sapo = item.select(".sapo").text();

                Truyen truyen = new Truyen(title, img, linkUrl, info, sapo, tags.html());
                System.out.println(truyen);


                if (!Jdbc.storyExists(truyen)) {
//                    System.out.println(truyen);
                    Jdbc.saveStory(truyen);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
