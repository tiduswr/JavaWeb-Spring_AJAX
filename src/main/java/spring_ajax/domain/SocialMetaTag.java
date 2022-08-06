package spring_ajax.domain;

import java.io.Serializable;

public class SocialMetaTag implements Serializable{

    private String site;
    private String title;
    private String url;
    private String image;

    public SocialMetaTag(String site, String title, String url, String image) {
        this.site = site;
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public SocialMetaTag() {}

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
