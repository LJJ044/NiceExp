package entity;

public class PageHistory {
    private String title;
    private String url;
    private int logo;
    public PageHistory(int logo, String title) {
        this.logo=logo;
        this.title=title;
    }
    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public PageHistory() {
        this.title = title;
        this.url = url;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
