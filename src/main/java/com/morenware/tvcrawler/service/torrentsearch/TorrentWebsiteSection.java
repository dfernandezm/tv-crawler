package com.morenware.tvcrawler.service.torrentsearch;

import com.morenware.tvcrawler.persistence.domain.TorrentContentType;

/**
 * Created by david on 09/11/2015.
 */
public class TorrentWebsiteSection {

    // Name for the section
    private String name;

    // TV_SHOWS, MOVIES
    private TorrentSearchSiteSection sectionType;
    private String sectionLink;

    private TorrentContentType contentType;
    private TorrentDateRepresentationType dateType;

    // Listing section
    private String listModeParam;
    private String totalNumberOfElementsRegex;
    private String pagerParameter;
    private String totalNumberOfPagesRegex;
    private String orderDateParam;
    private String totalNumberOfPagesSelector;

    // If this is present, it is a chain of selector of links that need to clicked to get to the torrent link in a listing
    // page. Each intermediate page should be checked for torrent data, as it is usually dispersed
    private String clickThroughSelectorsChain;

    // Torrent data
    private TorrentLinkType linkType;
    private Boolean torrentLinksAndDataInSamePage;
    private String torrentDataListSelector;
    private String headerSelector;
    private String torrentTitleSelector; // text
    private String torrentFileOrMagnetLinkSelector; // href

    // Torrent attributes
    private String dateSelector;
    private String dateRegex;
    private String seedsSelector;
    private String sizeSelector;
    private String qualitySelector;

    // Non-listing page: semi-colon separated list of links to get to the torrent link
    private String alphabetOrderingLinkSelectors;


    public String getTotalNumberOfPagesSelector() {
        return totalNumberOfPagesSelector;
    }

    public void setTotalNumberOfPagesSelector(String totalNumberOfPagesSelector) {
        this.totalNumberOfPagesSelector = totalNumberOfPagesSelector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TorrentSearchSiteSection getSectionType() {
        return sectionType;
    }

    public void setSectionType(TorrentSearchSiteSection sectionType) {
        this.sectionType = sectionType;
    }

    public TorrentContentType getContentType() {
        return contentType;
    }

    public void setContentType(TorrentContentType contentType) {
        this.contentType = contentType;
    }

    public String getSectionLink() {
        return sectionLink;
    }

    public void setSectionLink(String sectionLink) {
        this.sectionLink = sectionLink;
    }

    public TorrentDateRepresentationType getDateType() {
        return dateType;
    }

    public void setDateType(TorrentDateRepresentationType dateType) {
        this.dateType = dateType;
    }

    public String getListModeParam() {
        return listModeParam;
    }

    public void setListModeParam(String listModeParam) {
        this.listModeParam = listModeParam;
    }

    public String getTotalNumberOfElementsRegex() {
        return totalNumberOfElementsRegex;
    }

    public void setTotalNumberOfElementsRegex(String totalNumberOfElementsRegex) {
        this.totalNumberOfElementsRegex = totalNumberOfElementsRegex;
    }

    public String getPagerParameter() {
        return pagerParameter;
    }

    public void setPagerParameter(String pagerParameter) {
        this.pagerParameter = pagerParameter;
    }

    public String getTotalNumberOfPagesRegex() {
        return totalNumberOfPagesRegex;
    }

    public void setTotalNumberOfPagesRegex(String totalNumberOfPagesRegex) {
        this.totalNumberOfPagesRegex = totalNumberOfPagesRegex;
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

    public Boolean getTorrentLinksAndDataInSamePage() {
        return torrentLinksAndDataInSamePage;
    }

    public void setTorrentLinksAndDataInSamePage(Boolean torrentLinksAndDataInSamePage) {
        this.torrentLinksAndDataInSamePage = torrentLinksAndDataInSamePage;
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

    public String getDateRegex() {
        return dateRegex;
    }

    public void setDateRegex(String dateRegex) {
        this.dateRegex = dateRegex;
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

    public String getAlphabetOrderingLinkSelectors() {
        return alphabetOrderingLinkSelectors;
    }

    public void setAlphabetOrderingLinkSelectors(String alphabetOrderingLinkSelectors) {
        this.alphabetOrderingLinkSelectors = alphabetOrderingLinkSelectors;
    }

    public String getClickThroughSelectorsChain() {
        return clickThroughSelectorsChain;
    }

    public void setClickThroughSelectorsChain(String clickThroughSelectorsChain) {
        this.clickThroughSelectorsChain = clickThroughSelectorsChain;
    }

    public String getQualitySelector() {
        return qualitySelector;
    }

    public void setQualitySelector(String qualitySelector) {
        this.qualitySelector = qualitySelector;
    }

    public static TorrentWebsiteSection cloneSection(TorrentWebsiteSection section) {

        TorrentWebsiteSection newSection = new TorrentWebsiteSection();

        newSection.setPagerParameter(section.getPagerParameter());
        newSection.setListModeParam(section.getListModeParam());
        newSection.setOrderDateParam(section.getOrderDateParam());
        newSection.setTotalNumberOfElementsRegex(section.getTotalNumberOfElementsRegex());
        newSection.setTotalNumberOfPagesRegex(section.getTotalNumberOfPagesRegex());
        newSection.setTotalNumberOfPagesSelector(section.getTotalNumberOfPagesSelector());

        newSection.setSectionLink(section.getSectionLink());
        newSection.setDateType(section.getDateType());
        newSection.setLinkType(section.getLinkType());
        newSection.setContentType(section.getContentType());
        newSection.setTorrentDataListSelector(section.getTorrentDataListSelector());
        newSection.setHeaderSelector(section.getHeaderSelector());
        newSection.setTorrentTitleSelector(section.getTorrentTitleSelector());
        newSection.setTorrentLinksAndDataInSamePage(section.getTorrentLinksAndDataInSamePage());
        newSection.setTorrentFileOrMagnetLinkSelector(section.getTorrentFileOrMagnetLinkSelector());

        newSection.setDateSelector(section.getDateSelector());
        newSection.setDateRegex(section.getDateRegex());
        newSection.setSeedsSelector(section.getSeedsSelector());
        newSection.setSizeSelector(section.getSizeSelector());
        newSection.setAlphabetOrderingLinkSelectors(section.getAlphabetOrderingLinkSelectors());

        return newSection;
    }
}
