package bot.obj;

public class Truyen {
    String header;
    String img;
    String url;
    String info;
    String content;
    private String category;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Truyen(String header, String img, String url, String info, String content,String category) {
        this.header = header;
        this.url = url;
        this.info = info;
        this.img = img;
        this.content = content;
        this.category = category;
    }

    @Override
    public String toString() {
        return //"Truyen: \n" +
                "Tên Truyện: " + header + "\n" +
                        "urlImg: " + img + "\n" +
                        "url: " + url + "\n" +
                        "" + info + "\n" +
                        "Mô tả: " + content + "\n" +
                        "Thể loại: " + category + "\n"+
                        "---------------------------";
    }
}
