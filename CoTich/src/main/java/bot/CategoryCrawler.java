package bot;

import bot.obj.Category;
import database.Jdbc;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CategoryCrawler extends BaseBot{
    private Set<String> visitedGenres;
    public boolean isRunning = false;
    public CategoryCrawler(String baseUrl) {
        super(baseUrl, 5, 100);
        visitedGenres = new HashSet<>();
        isRunning = true;
    }

    @Override
    protected void crawl(String baseUrl) {
        super.crawl(baseUrl);
        for (String url : visitedUrls) {
            processCategory(url);
        }
    }
    protected void processCategory(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements genres = doc.select(".menuTop a");

            for (Element genre : genres) {
                String genreName = genre.text();
                String genreUrl = genre.absUrl("href");

                // Kiểm tra xem thể loại đã được lướt qua chưa
                if (!visitedGenres.contains(genreName)) {
                    visitedGenres.add(genreName); // Thêm thể loại vào danh sách đã trích xuất
                    System.out.println("Thể loại truyện: " + genreName + " - URL " + genreUrl);

                    Category category = new Category(genreName, genreUrl);

                    if (!Jdbc.categoryExists(category)) {
//                    System.out.println(category);
                        Jdbc.saveCategory(category);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi lục soát trang: " + e.getMessage());
        }
    }
}
