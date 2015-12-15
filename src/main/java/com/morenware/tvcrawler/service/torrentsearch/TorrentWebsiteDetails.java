package com.morenware.tvcrawler.service.torrentsearch;

import java.util.List;

/**
 * Created by david on 23/05/15.
 */
public class TorrentWebsiteDetails {

    private String baseUrl;
    private String language;
    private String siteName;
    private String siteId;
    private List<TorrentWebsiteSection> sections;

    private String listModeParam;
    private String orderDateParam;
    private TorrentLinkType linkType;
    private String totalNumberOfShowsRegex;
    private String moviesMainSectionLink;
    private String tvShowsMainSectionLink;

    // ENGLISH_AGE
    // SPANISH_AGE (in hora/s, dia/s, semanas, months)
    // PARTIAL_DATE day without year
    // FULL_DATE day, month, year
    private TorrentDateRepresentationType dateType;

    // Which criteria to use in order to decide if we already have this result
    private TorrentExistenceComparator existenceComparator;
    private String pagerParameter;
    private String totalNumberOfPagesSelector;

    // Torrent data specific CSS Selectors for JSoup
    private String torrentDataListSelector;
    private String headerSelector;
    private String torrentTitleSelector; // get the text
    private String torrentFileOrMagnetLinkSelector; // get href;
    private String dateSelector;

    // Regex to extract the date once the selector is matched, if null the date is directly the text
    private String dateRegex;
    private String seedsSelector;
    private String sizeSelector;

    // semicolon separated list of link selectors to get to the torrent link
    private String tvShowsAlphabetOrderingLinkSelectors;
    private String moviesAlphabetOrderingLinkSelectors;

    // This flag indicates whether or not torrent links and its data (Date, seeds, etc) are in the same page or not
    // This will determine how the selectors for the data are written, relative or absolute to that page
    // If the site is a listing, this is always true. If the site is alphabetical ordering torrent links and data can be
    // scattered
    private Boolean moviesTorrentLinksAndDataInSamePage;
    private Boolean tvShowsTorrentLinksAndDataInSamePage;

    public Boolean getMoviesTorrentLinksAndDataInSamePage() {
        return moviesTorrentLinksAndDataInSamePage;
    }

    public void setMoviesTorrentLinksAndDataInSamePage(Boolean moviesTorrentLinksAndDataInSamePage) {
        this.moviesTorrentLinksAndDataInSamePage = moviesTorrentLinksAndDataInSamePage;
    }

    public String getMoviesAlphabetOrderingLinkSelectors() {
        return moviesAlphabetOrderingLinkSelectors;
    }

    public void setMoviesAlphabetOrderingLinkSelectors(String moviesAlphabetOrderingLinkSelectors) {
        this.moviesAlphabetOrderingLinkSelectors = moviesAlphabetOrderingLinkSelectors;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getMoviesMainSectionLink() {
        return moviesMainSectionLink;
    }

    public void setMoviesMainSectionLink(String moviesMainSectionLink) {
        this.moviesMainSectionLink = moviesMainSectionLink;
    }

    public String getTvShowsMainSectionLink() {
        return tvShowsMainSectionLink;
    }

    public void setTvShowsMainSectionLink(String tvShowsMainSectionLink) {
        this.tvShowsMainSectionLink = tvShowsMainSectionLink;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getListModeParam() {
        return listModeParam;
    }

    public void setListModeParam(String listModeParam) {
        this.listModeParam = listModeParam;
    }

    public String getOrderDateParam() {
        return orderDateParam;
    }

    public void setOrderDateParam(String orderDateParam) {
        this.orderDateParam = orderDateParam;
    }

    public TorrentLinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(TorrentLinkType linkType) {
        this.linkType = linkType;
    }

    public String getTotalNumberOfShowsRegex() {
        return totalNumberOfShowsRegex;
    }

    public void setTotalNumberOfShowsRegex(String totalNumberOfShowsRegex) {
        this.totalNumberOfShowsRegex = totalNumberOfShowsRegex;
    }

    public TorrentDateRepresentationType getDateType() {
        return dateType;
    }

    public void setDateType(TorrentDateRepresentationType dateType) {
        this.dateType = dateType;
    }

    public TorrentExistenceComparator getExistenceComparator() {
        return existenceComparator;
    }

    public void setExistenceComparator(TorrentExistenceComparator existenceComparator) {
        this.existenceComparator = existenceComparator;
    }

    public String getPagerParameter() {
        return pagerParameter;
    }

    public void setPagerParameter(String pagerParameter) {
        this.pagerParameter = pagerParameter;
    }

    public String getTotalNumberOfPagesSelector() {
        return totalNumberOfPagesSelector;
    }

    public void setTotalNumberOfPagesSelector(String totalNumberOfPagesSelector) {
        this.totalNumberOfPagesSelector = totalNumberOfPagesSelector;
    }

    public String getTorrentDataListSelector() {
        return torrentDataListSelector;
    }

    public void setTorrentDataListSelector(String torrentDataListSelector) {
        this.torrentDataListSelector = torrentDataListSelector;
    }

    public String getHeaderSelector() {
        return headerSelector;
    }

    public void setHeaderSelector(String headerSelector) {
        this.headerSelector = headerSelector;
    }

    public String getTorrentTitleSelector() {
        return torrentTitleSelector;
    }

    public void setTorrentTitleSelector(String torrentTitleSelector) {
        this.torrentTitleSelector = torrentTitleSelector;
    }

    public String getTorrentFileOrMagnetLinkSelector() {
        return torrentFileOrMagnetLinkSelector;
    }

    public void setTorrentFileOrMagnetLinkSelector(String torrentFileOrMagnetLinkSelector) {
        this.torrentFileOrMagnetLinkSelector = torrentFileOrMagnetLinkSelector;
    }

    public String getDateSelector() {
        return dateSelector;
    }

    public void setDateSelector(String dateSelector) {
        this.dateSelector = dateSelector;
    }

    public String getSeedsSelector() {
        return seedsSelector;
    }

    public void setSeedsSelector(String seedsSelector) {
        this.seedsSelector = seedsSelector;
    }

    public String getSizeSelector() {
        return sizeSelector;
    }

    public void setSizeSelector(String sizeSelector) {
        this.sizeSelector = sizeSelector;
    }

    public String getTvShowsAlphabetOrderingLinkSelectors() {
        return tvShowsAlphabetOrderingLinkSelectors;
    }

    public void setTvShowsAlphabetOrderingLinkSelectors(String tvShowsAlphabetOrderingLinkSelectors) {
        this.tvShowsAlphabetOrderingLinkSelectors = tvShowsAlphabetOrderingLinkSelectors;
    }

    public Boolean getTvShowsTorrentLinksAndDataInSamePage() {
        return tvShowsTorrentLinksAndDataInSamePage;
    }

    public void setTvShowsTorrentLinksAndDataInSamePage(Boolean tvShowsTorrentLinksAndDataInSamePage) {
        this.tvShowsTorrentLinksAndDataInSamePage = tvShowsTorrentLinksAndDataInSamePage;
    }

    public String getDateRegex() {
        return dateRegex;
    }

    public void setDateRegex(String dateRegex) {
        this.dateRegex = dateRegex;
    }


    public List<TorrentWebsiteSection> getSections() {
        return sections;
    }

    public void setSections(List<TorrentWebsiteSection> sections) {
        this.sections = sections;
    }
}
