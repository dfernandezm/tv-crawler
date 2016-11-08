package com.morenware.tvcrawler.persistence.domain;
import org.joda.time.LocalDateTime;

/**
 * Created by david on 23/05/15.
 */
public class TorrentSearchResult {

    private Integer id;
    private String title;
    private String magnetLink;
    private LocalDateTime date;
    private LocalDateTime dateFound;
    private TorrentState state;
    private TorrentContentType contentType;
    private TorrentOrigin origin;
    private String torrentFileLink;
    private Integer size;
    private Integer seeds;
    private String searchHash;
    private String siteId;
    private String language;
    private String quality;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSearchHash() {
        return searchHash;
    }

    public void setSearchHash(String searchHash) {
        this.searchHash = searchHash;
    }

    public TorrentSearchResult() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMagnetLink() {
        return magnetLink;
    }

    public void setMagnetLink(String magnetLink) {
        this.magnetLink = magnetLink;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDateFound() {
        return dateFound;
    }

    public void setDateFound(LocalDateTime dateFound) {
        this.dateFound = dateFound;
    }

    public TorrentState getState() {
        return state;
    }

    public void setState(TorrentState state) {
        this.state = state;
    }

    public TorrentContentType getContentType() {
        return contentType;
    }

    public void setContentType(TorrentContentType contentType) {
        this.contentType = contentType;
    }

    public TorrentOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(TorrentOrigin origin) {
        this.origin = origin;
    }

    public String getTorrentFileLink() {
        return torrentFileLink;
    }

    public void setTorrentFileLink(String torrentFileLink) {
        this.torrentFileLink = torrentFileLink;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getSeeds() {
        return seeds;
    }

    public void setSeeds(Integer seeds) {
        this.seeds = seeds;
    }
}
