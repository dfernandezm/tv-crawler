package com.morenware.tvcrawler.persistence.domain;

import com.morenware.tvcrawler.persistence.dao.Identifiable;
import org.joda.time.DateTime;

/**
 * Created by david on 17/05/15.
 */
public class Torrent implements Identifiable<Integer> {

    private Integer id;
    private String guid;
    private TorrentContentType contentType;
    private DateTime date;
    private String filePath;
    private String hash;
    private String magnetLink;
    private TorrentOrigin origin;
    private Double percentDone;
    private String renamedPath;
    private Integer seeds;
    private Integer size;
    private TorrentState state;
    private String title;
    private String torrentFileLink;
    private String torrentName;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public TorrentContentType getContentType() {
        return contentType;
    }

    public void setContentType(TorrentContentType contentType) {
        this.contentType = contentType;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMagnetLink() {
        return magnetLink;
    }

    public void setMagnetLink(String magnetLink) {
        this.magnetLink = magnetLink;
    }

    public TorrentOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(TorrentOrigin origin) {
        this.origin = origin;
    }

    public Double getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(Double percentDone) {
        this.percentDone = percentDone;
    }

    public String getRenamedPath() {
        return renamedPath;
    }

    public void setRenamedPath(String renamedPath) {
        this.renamedPath = renamedPath;
    }

    public Integer getSeeds() {
        return seeds;
    }

    public void setSeeds(Integer seeds) {
        this.seeds = seeds;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public TorrentState getState() {
        return state;
    }

    public void setState(TorrentState state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTorrentFileLink() {
        return torrentFileLink;
    }

    public void setTorrentFileLink(String torrentFileLink) {
        this.torrentFileLink = torrentFileLink;
    }

    public String getTorrentName() {
        return torrentName;
    }

    public void setTorrentName(String torrentName) {
        this.torrentName = torrentName;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
