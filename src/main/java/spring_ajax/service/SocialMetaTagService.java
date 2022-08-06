package spring_ajax.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spring_ajax.domain.SocialMetaTag;

import java.io.IOException;

@Service
public class SocialMetaTagService {

    private static Logger log = LoggerFactory.getLogger(SocialMetaTagService.class);

    public SocialMetaTag getSocialMetaTagByUrl(String url){
        SocialMetaTag tag = getTwitterCardByURL(url);
        if(isEmpty(tag)) tag = getOpenGraphByURL(url);
        if(isEmpty(tag)) tag = null;
        return tag;
    }

    private SocialMetaTag getOpenGraphByURL(String url){
        SocialMetaTag tag = new SocialMetaTag();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .header("Content-Type","application/x-www-form-urlencoded")
                    .get();
            tag.setTitle(doc.head().select("meta[property=og:title]").attr("content"));
            tag.setSite(doc.head().select("meta[property=og:site_name]").attr("content"));
            tag.setImage(doc.head().select("meta[property=og:image]").attr("content"));
            tag.setUrl(doc.head().select("meta[property=og:url]").attr("content"));
            if(tag.getUrl() == null || tag.getUrl().isEmpty()) tag.setUrl(url);
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
        }
        return tag;
    }

    private SocialMetaTag getTwitterCardByURL(String url){
        SocialMetaTag tag = new SocialMetaTag();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .header("Content-Type","application/x-www-form-urlencoded")
                    .get();
            tag.setTitle(doc.head().select("meta[name=twitter:title]").attr("content"));
            tag.setSite(doc.head().select("meta[name=twitter:site]").attr("content"));
            if(tag.getSite() != null || !tag.getSite().isEmpty()) tag.setSite(tag.getSite().replace("@", ""));
            tag.setImage(doc.head().select("meta[name=twitter:image]").attr("content"));
            tag.setUrl(doc.head().select("meta[name=twitter:url]").attr("content"));
            if(tag.getUrl() == null || tag.getUrl().isEmpty()) tag.setUrl(url);
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());
        }
        return tag;
    }

    private boolean isEmpty(SocialMetaTag tag){
        return  tag.getSite() == null || tag.getSite().isEmpty() ||
                tag.getUrl() == null || tag.getUrl().isEmpty() ||
                tag.getTitle() == null || tag.getTitle().isEmpty() ||
                tag.getImage() == null || tag.getImage().isEmpty();
    }

}
