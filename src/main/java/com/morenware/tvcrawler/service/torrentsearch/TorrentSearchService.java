package com.morenware.tvcrawler.service.torrentsearch;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.morenware.tvcrawler.persistence.domain.TorrentContentType;
import com.morenware.tvcrawler.persistence.domain.TorrentSearchResult;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by david on 23/05/15.
 */
public class TorrentSearchService {

    private Logger log = LoggerFactory.getLogger(TorrentSearchService.class.getName());

    private static final Integer CONNECTION_RETRY_COUNT = 5;

    private static final Integer PAUSE_TIME_BETWEEN_TRIES = 1200;

    private static String crawlerBasePath = "/tmp/crawler/cache";

    private static final String HASH_REGEX_IN_MAGNET_LINK = "";

    private static final String TITLE_IN_MAGNET_LINK_REGEX = "&dn=(.*)&";

    private static final Integer MAXIMUM_NUMBER_OF_PAGES = 350;

    // Case insensitive (?i)
    private static final String TORRENT_SIZE_REGEX = "(?i)([0-9]+(?:\\.)*(?:[0-9]+)*)(?:.*)(GB|MB|KB)";

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public String createInitialSearchLink(String baseUrl, String baseSectionLink, String extraParameters) {
        return baseUrl + baseSectionLink + extraParameters;
    }

    // Has a RSS
    public void crawlDivxTotal() {

        TorrentWebsiteDetails torrentWebsiteDetails1 = new TorrentWebsiteDetails();
        torrentWebsiteDetails1.setLanguage("es");
        torrentWebsiteDetails1.setSiteId("DT");
        torrentWebsiteDetails1.setBaseUrl("http://www.divxtotal.com");

        TorrentWebsiteSection tvShowsSection = new TorrentWebsiteSection();

        tvShowsSection.setPagerParameter(null);
        tvShowsSection.setListModeParam(null);
        tvShowsSection.setOrderDateParam(null);
        tvShowsSection.setTotalNumberOfElementsRegex(null);
        tvShowsSection.setTotalNumberOfPagesRegex(null);

        tvShowsSection.setSectionLink("/series/");
        tvShowsSection.setDateType(TorrentDateRepresentationType.FULL_DATE);
        tvShowsSection.setLinkType(TorrentLinkType.TORRENT_FILE);
        tvShowsSection.setContentType(TorrentContentType.TV_SHOW);
        tvShowsSection.setTorrentDataListSelector("table.fichserietabla tr");
        tvShowsSection.setHeaderSelector("td.capitulofecha:contains(Fecha)");
        tvShowsSection.setTorrentTitleSelector("td.capitulonombre a"); // text
        tvShowsSection.setTorrentLinksAndDataInSamePage(true);
        tvShowsSection.setTorrentFileOrMagnetLinkSelector("td.capitulonombre a");

        tvShowsSection.setDateSelector("td.capitulofecha");
        tvShowsSection.setDateRegex("(\\d{2}\\-\\d{2}\\-\\d{4})");
        tvShowsSection.setSeedsSelector(null);
        tvShowsSection.setSizeSelector(null);
        tvShowsSection.setAlphabetOrderingLinkSelectors("li[class~=li_listadoseries] a[href~=/series/]");

        TorrentWebsiteSection moviesSection = TorrentWebsiteSection.cloneSection(tvShowsSection);
        moviesSection.setName("Movies DivXTotal");
        moviesSection.setSectionLink("/peliculas/");
        moviesSection.setPagerParameter("pagina/{pageNumber}/");
        moviesSection.setListModeParam("");
        moviesSection.setOrderDateParam("");
        moviesSection.setAlphabetOrderingLinkSelectors(null);
        moviesSection.setTorrentDataListSelector("ul.section_list > li");
        moviesSection.setHeaderSelector("li.section_item_tit");
        moviesSection.setContentType(TorrentContentType.MOVIE);
        moviesSection.setSectionType(TorrentSearchSiteSection.MOVIES);

        // We don't know, visit maximum number of pages
        moviesSection.setTotalNumberOfElementsRegex(null);
        moviesSection.setTotalNumberOfPagesRegex(null);

        moviesSection.setTorrentLinksAndDataInSamePage(false);
        moviesSection.setClickThroughSelectorsChain("p.seccontnom a");
        moviesSection.setSizeSelector("p:eq(3)");
        moviesSection.setTorrentTitleSelector("p.seccontnom a"); // text
        moviesSection.setTorrentFileOrMagnetLinkSelector("div.ficha_link_det > h3 > a");
        moviesSection.setDateSelector("p:eq(2)");
        moviesSection.setDateRegex("(\\d{2}\\-\\d{2}\\-\\d{4})");

        List<TorrentWebsiteSection> torrentWebsiteSections = new ArrayList<>();
        torrentWebsiteSections.add(moviesSection);
        torrentWebsiteDetails1.setSections(torrentWebsiteSections);

        crawlWebsiteAndPersistResults(torrentWebsiteDetails1);
    }

    public void crawlMejorTorrent() {

        TorrentWebsiteDetails torrentWebsiteDetails1 = new TorrentWebsiteDetails();
        torrentWebsiteDetails1.setLanguage("es");
        torrentWebsiteDetails1.setSiteId("MT");
        torrentWebsiteDetails1.setBaseUrl("http://www.mejortorrent.com");

        // If no list, we need to check in every step if the date is present
        //torrentWebsiteDetails1.setDateRegex("Fecha\\:(.*)");

        TorrentWebsiteSection tvShowsSection = new TorrentWebsiteSection();

        tvShowsSection.setPagerParameter(null);
        tvShowsSection.setListModeParam(null);
        tvShowsSection.setOrderDateParam(null);
        tvShowsSection.setTotalNumberOfElementsRegex(null);
        tvShowsSection.setTotalNumberOfPagesRegex(null);
        tvShowsSection.setTotalNumberOfPagesSelector(null);

        tvShowsSection.setSectionLink("/torrents-de-series.html");
        tvShowsSection.setDateType(TorrentDateRepresentationType.PARTIAL_DATE);
        tvShowsSection.setLinkType(TorrentLinkType.TORRENT_FILE);
        tvShowsSection.setContentType(TorrentContentType.TV_SHOW);
        tvShowsSection.setTorrentDataListSelector(null);
        tvShowsSection.setHeaderSelector(null);
        tvShowsSection.setTorrentTitleSelector(null);
        tvShowsSection.setTorrentLinksAndDataInSamePage(false);
        tvShowsSection.setTorrentFileOrMagnetLinkSelector("a[href~=uploads/torrents]");

        tvShowsSection.setDateSelector("div:contains(Fecha:)");
        tvShowsSection.setDateRegex("Fecha\\:(\\d{2}\\-\\d{2}\\-\\d{2})");
        tvShowsSection.setSeedsSelector(null);
        tvShowsSection.setSizeSelector(null);
        tvShowsSection.setAlphabetOrderingLinkSelectors("a[href~=series-letra];a[href~=serie-descargar-torrents];a[href~=serie-episodio-descargar-torrent];a[href~=secciones.php\\?sec=descargas]");

        List<TorrentWebsiteSection> torrentWebsiteSections = new ArrayList<>();
        torrentWebsiteSections.add(tvShowsSection);
        torrentWebsiteDetails1.setSections(torrentWebsiteSections);

        crawlWebsiteAndPersistResults(torrentWebsiteDetails1);
    }

    private List<TorrentSearchResult> processElements(final List<Future> futures, Elements elements,
                                                      final TorrentWebsiteDetails siteDetails,
                                                      final TorrentWebsiteSection currentSection,
                                                      final TorrentContentType contentType, final Integer index,
                                                      final String dateAsString, final String[] cssSelectors,
                                                      final String currentDate) {

        final List<TorrentSearchResult> results = new ArrayList<>();

        for (final Element element : elements) {

            Runnable elementRunnable = new Runnable() {

                @Override
                public void run() {

                    try {

                        Document doc = visitLinkElementAndGetDocumentFromHtml(element, siteDetails.getBaseUrl(),
                                siteDetails.getSiteId(),
                                dateAsString, true);

                        List<TorrentSearchResult> currentResults = new ArrayList<>();

                        if (index == cssSelectors.length) { // last selector, torrent Links are here

                            if (currentSection.getTorrentLinksAndDataInSamePage()) {

                                // The last selector shows a page where there is a listing, so there is a top level selector for the list
                                // and then relative selectors for data, like in a listing page type.
                                // We interpret the torrentLinkSelector, date selector, etc. as relatives to the torrentDataListSelector
                                // From here on, we consider this a single listing page
                                Elements listingElements = doc.select(currentSection.getTorrentDataListSelector());

                                try {
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    // Nothing to do
                                }

                                for (Element listingElement : listingElements) {
                                    currentResults.add(extractTorrentDataFromListElement(siteDetails, currentSection, contentType, listingElement));
                                }

                                persistTorrentResults(currentResults);
                                results.addAll(currentResults);

                            } else {

                                // The last selector shows a page with just a torrent link in it, try to select it only
                                Elements torrentLinkElements = doc.select(currentSection.getTorrentFileOrMagnetLinkSelector());

                                for (Element torrentLinkElement : torrentLinkElements) {

                                    TorrentSearchResult torrentResult = new TorrentSearchResult();
                                    String torrentLink = torrentLinkElement.attr("href");

                                    if (currentSection.getLinkType() == TorrentLinkType.TORRENT_FILE) {
                                        torrentLink = siteDetails.getBaseUrl() + "/" + torrentLink;
                                        torrentResult.setTorrentFileLink(torrentLink);
                                        torrentResult.setTitle(extractTitleFromTorrentFile(torrentLink));
                                    } else {
                                        torrentResult.setMagnetLink(torrentLink);
                                        torrentResult.setTitle(extractTitleFromMagnetLink(torrentLink));
                                    }

                                    String dateToSend = currentDate;
                                    // Check for the date
                                    if (currentDate == null) {
                                        Elements dateElements = doc.select(currentSection.getDateSelector());
                                        if (dateElements.size() > 0) {
                                            Element dateElement = dateElements.get(0);
                                            dateToSend = dateElement.text();
                                        }
                                    }

                                    if (currentDate != null) {
                                        //TODO: generalize this, there might be some portions that need to be extracted
                                        // i.e check if we have a regex for the date to parse (add to site details)
                                        if (siteDetails.getDateRegex() != null) {
                                            Pattern datePattern = Pattern.compile(currentSection.getDateRegex());
                                            Matcher dateMatcher = datePattern.matcher(currentDate);
                                            if (dateMatcher.find()) {
                                                String dateString = dateMatcher.group(1);
                                                torrentResult.setDate(LocalDateTime.parse(dateString.trim()));
                                            }
                                        }
                                    }

                                    log.info("[] Getting torrentLink ==> " + torrentResult.getTorrentFileLink() + " - " + torrentResult.getMagnetLink() + ", date: " + torrentResult.getDate().toString(DateTimeFormat.shortDateTime()));

                                    currentResults.add(torrentResult);
                                    persistTorrentResults(currentResults);

                                    results.add(torrentResult);

                                }
                            }
                        } else {

                            Elements newElements = doc.select(cssSelectors[index]);
                            String dateToSend = currentDate;

                            if (newElements.size() > 0) {
                                if (!currentSection.getTorrentLinksAndDataInSamePage()) {

                                    // Check for the date
                                    Elements dateElements = doc.select(currentSection.getDateSelector());
                                    if (dateElements.size() > 0) {
                                        Element dateElement = dateElements.get(0);
                                        dateToSend = dateElement.text();
                                    }
                                }

                                try {
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    // Nothing to do
                                }

                                results.addAll(processElements(futures, newElements, siteDetails, currentSection, contentType, index + 1, dateAsString, cssSelectors, dateToSend));
                            }
                        }

                    } catch (Throwable t) {
                        log.warn("Error retrieving elements for element - ", t);
                    }
                }
            };
            
            Future<?> future = executorService.submit(elementRunnable);
            futures.add(future);
        }

        return results;
    }

    private Document visitLinkElementAndGetDocumentFromHtml(Element element, String baseUrl, String siteId, String dateAsString, Boolean invalidateCache) {
        String fullLink = baseUrl + "/" + element.attr("href");
        String newHtml = getHtmlFromPageAndCache(fullLink, null, siteId, TorrentSearchSiteSection.TV_SHOWS, dateAsString, invalidateCache);
        return Jsoup.parse(newHtml);
    }


    public void crawlEliteTorrent() {

        TorrentWebsiteDetails torrentWebsiteDetails = new TorrentWebsiteDetails();
        torrentWebsiteDetails.setLanguage("es");
        torrentWebsiteDetails.setSiteId("ET");
        torrentWebsiteDetails.setBaseUrl("http://www.elitetorrent.net");

        TorrentWebsiteSection tvShowsSection = new TorrentWebsiteSection();
        tvShowsSection.setName("TV Shows");

        tvShowsSection.setPagerParameter("/pag:{pageNumber}");
        tvShowsSection.setListModeParam("/modo:listado");
        tvShowsSection.setOrderDateParam("/orden:fecha");
        tvShowsSection.setTotalNumberOfElementsRegex(null);
        tvShowsSection.setTotalNumberOfPagesRegex("Series\\s+\\(total\\s+(.*)\\)");
        tvShowsSection.setTotalNumberOfPagesSelector("div.paginacion > a");

        tvShowsSection.setSectionLink("/categoria/4/series");
        tvShowsSection.setSectionType(TorrentSearchSiteSection.TV_SHOWS);
        tvShowsSection.setDateType(TorrentDateRepresentationType.SPANISH_AGE);
        tvShowsSection.setLinkType(TorrentLinkType.TORRENT_FILE);
        tvShowsSection.setContentType(TorrentContentType.TV_SHOW);
        tvShowsSection.setTorrentDataListSelector("table.fichas-listado tr");
        tvShowsSection.setHeaderSelector(".cabecera");
        tvShowsSection.setTorrentTitleSelector("td.nombre > a.nombre");
        tvShowsSection.setTorrentLinksAndDataInSamePage(true);
        tvShowsSection.setTorrentFileOrMagnetLinkSelector("td.nombre > a.icono-bajar");

        tvShowsSection.setDateSelector("td.fecha");
        tvShowsSection.setDateRegex(null);
        tvShowsSection.setSeedsSelector("td.semillas");
        tvShowsSection.setSizeSelector(null);
        tvShowsSection.setAlphabetOrderingLinkSelectors(null);

        // Movies - Regular Quality
        TorrentWebsiteSection moviesSection = new TorrentWebsiteSection();
        moviesSection.setName("Peliculas");
        moviesSection.setSectionType(TorrentSearchSiteSection.MOVIES);

        moviesSection.setPagerParameter("/pag:{pageNumber}");
        moviesSection.setListModeParam("/modo:listado");
        moviesSection.setOrderDateParam("/orden:fecha");
        moviesSection.setTotalNumberOfElementsRegex(null);
        moviesSection.setTotalNumberOfPagesRegex("Series\\s+\\(total\\s+(.*)\\)");
        moviesSection.setTotalNumberOfPagesSelector("div.paginacion > a");

        moviesSection.setSectionLink("/categoria/2/peliculas");
        moviesSection.setDateType(TorrentDateRepresentationType.SPANISH_AGE);
        moviesSection.setLinkType(TorrentLinkType.TORRENT_FILE);
        moviesSection.setContentType(TorrentContentType.MOVIE);
        moviesSection.setTorrentDataListSelector("table.fichas-listado tr");
        moviesSection.setHeaderSelector(".cabecera");
        moviesSection.setTorrentTitleSelector("td.nombre > a.nombre");
        moviesSection.setTorrentLinksAndDataInSamePage(true);
        moviesSection.setTorrentFileOrMagnetLinkSelector("td.nombre > a.icono-bajar");

        moviesSection.setDateSelector("td.fecha");
        moviesSection.setDateRegex(null);
        moviesSection.setSeedsSelector("td.semillas");
        moviesSection.setSizeSelector(null);
        moviesSection.setAlphabetOrderingLinkSelectors(null);

        // Movies - HD Quality
        TorrentWebsiteSection hdMoviesSection = TorrentWebsiteSection.cloneSection(moviesSection);
        hdMoviesSection.setName("Peliculas HD");
        hdMoviesSection.setSectionLink("/categoria/13/peliculas-hdrip");

        // Movies - Estrenos
        TorrentWebsiteSection newMoviesSection = TorrentWebsiteSection.cloneSection(moviesSection);
        newMoviesSection.setSectionLink("/categoria/1/estrenos");

        // Movies - MicroHD
        TorrentWebsiteSection microHdMoviesSection = TorrentWebsiteSection.cloneSection(moviesSection);
        microHdMoviesSection.setSectionLink("/categoria/17/peliculas-microhd");


        ///categoria/17/peliculas-microhd

        List<TorrentWebsiteSection> torrentWebsiteSections = new ArrayList<>();
        torrentWebsiteSections.add(tvShowsSection);
        torrentWebsiteSections.add(moviesSection);
        torrentWebsiteSections.add(hdMoviesSection);
        torrentWebsiteSections.add(newMoviesSection);
        torrentWebsiteSections.add(microHdMoviesSection);

        torrentWebsiteDetails.setSections(torrentWebsiteSections);

        crawlWebsiteAndPersistResults(torrentWebsiteDetails);

    }

    private String extractTitleFromTorrentFile(String torrentFile) {
        //  http://www.mejortorrent.com//uploads/torrents/series/Alfred_1x09_No.vuelvas.torrent - null, date: 10/15/10 12:00 AM
        String title = torrentFile.substring(torrentFile.lastIndexOf('/') + 1, torrentFile.length());
        title = title.replaceAll("\\.torrent", "");
        title = title.replaceAll("[-\\._]", " ");
        return title;
    }


    private void crawlWebsiteAndPersistResults(TorrentWebsiteDetails torrentWebsiteDetails) {

        log.info("Starting to crawl site " + torrentWebsiteDetails.getBaseUrl());

        long overallStartTime = System.currentTimeMillis();

        List<Future> futures = new ArrayList<>();
        List<TorrentSearchResult> results = new ArrayList<>();

        for (final TorrentWebsiteSection section : torrentWebsiteDetails.getSections()) {

            log.info("Starting to crawl section " + section.getName());

            boolean isListingSection = true;

            // The section has a chain of pages to get to the links, it is not a listing based one
            if (section.getAlphabetOrderingLinkSelectors() != null) {
                isListingSection = false;
            }

            final TorrentSearchSiteSection sectionType = section.getSectionType();
            String mainSectionLink = section.getSectionLink();

            String sectionBaseLink = "";

            // If the site has a chain of pages to get to the links, it is not a listing based one
            if (isListingSection) {

                log.info("Section " + torrentWebsiteDetails.getBaseUrl() + " is a LISTING site - paged site");

                // Check if it is the first time we crawl this site
                // If it is the first time, we crawl everything
                // If we already have content, crawl at most 10 pages of content (as this is daily) to fetch new content
                // If we cannot get a list ordered by date, we have to crawl everything every time

                sectionBaseLink = createInitialSearchLink(torrentWebsiteDetails.getBaseUrl(),
                        mainSectionLink,
                        (section.getListModeParam() + section.getOrderDateParam()));

            } else {

                log.info("Site " + torrentWebsiteDetails.getBaseUrl() + " is not a LISTING site - chain of pages");
                sectionBaseLink = createInitialSearchLink(torrentWebsiteDetails.getBaseUrl(),
                        mainSectionLink, "");
            }

            final String baseLink = sectionBaseLink;

            // Get first page HTML and cache it
            DateTime dateToday = new DateTime();
            final String dateAsString = dateToday.withTimeAtStartOfDay().toString();

            String html = null;

            try {

                html = getHtmlFromPageAndCache(baseLink, 1, torrentWebsiteDetails.getSiteId(),
                        sectionType, dateAsString, true);

            } catch (Throwable t) {
                log.error("Could not get HTML from first page - link  " + baseLink + " moving to next section");
            }


            if (html != null) {

                if (isListingSection) {

                    final Integer totalPages = getTotalNumberOfPagesToVisit(section.getTotalNumberOfPagesSelector(), html);
                    final List<TorrentSearchResult> finalResults = results;
                    final TorrentWebsiteDetails twt = torrentWebsiteDetails;
                    final TorrentContentType sectionContentType = section.getContentType();

                    // First page
                    results.addAll(crawlHtmlForTorrentData(torrentWebsiteDetails, section, sectionContentType, html));

                    for (int i = 2; i <= totalPages; i++) {

                        final int finali = i;

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {

                                log.info("[CRAWLER-LISTING] Retrieving HTML from page " + finali);
                                String linkToConnect = baseLink + section.getPagerParameter().replace("{pageNumber}", finali + "");

                                try {

                                    long startConnectingTime = System.currentTimeMillis();

                                    String htmli = getHtmlFromPageAndCache(linkToConnect, finali, twt.getSiteId(),
                                            sectionType, dateAsString, true);

                                    log.info("It took " + (System.currentTimeMillis() - startConnectingTime) + " ms to connect to link " + linkToConnect);

                                    // Crawl cached page: extract title, torrent link, date, seeds, size
                                    finalResults.addAll(crawlHtmlForTorrentData(twt, section, sectionContentType, htmli));

                                    throttleThreadForMillis(200);

                                } catch (Throwable t) {
                                    log.warn("Error getting content from link " + linkToConnect, t);
                                }
                            }
                        };

                        Future<?> future = executorService.submit(r);
                        futures.add(future);

                        log.info("[CRAWLER-LISTING] Submitting job to Executor, count " + futures.size());

                    }

                } else {

                    // We don't know how many pages need to visit, will go till the end link
                    Document doc = Jsoup.parse(html);

                    String[] linkSelectors = section.getAlphabetOrderingLinkSelectors().split("[;]");

                    Elements elements = doc.select(linkSelectors[0]);

                    results = processElements(futures, elements, torrentWebsiteDetails, section, section.getContentType(), 1, dateAsString, linkSelectors, null);

                }
            }

            Iterator<Future> futureIterator = futures.iterator();

            boolean terminated = false;
            while (!terminated) {

                log.info("Waiting for threads to terminate, remaining --" + futures.size());

                while (futureIterator.hasNext()) {
                    Future<?> future = futureIterator.next();
                    if (future.isDone() || future.isCancelled()) {
                        futureIterator.remove();
                    }
                }

                if (futures.size() > 0) {
                    futureIterator = futures.iterator();
                } else {
                    terminated = true;
                }

                throttleThreadForMillis(500);
            }

            log.info("Finishing to crawl section -- " + mainSectionLink + " --- " + results.size() + " found so far");

        }

        // Finish
        log.info("[SEARCH-CRAWL] It took " + (System.currentTimeMillis() - overallStartTime) + " ms to crawl entire website, found " + results.size() + " torrents.");
    }


    private String listingSiteSectionHasBeenCrawledRecently(String sitedId, TorrentWebsiteSection section) {
        //TODO: if recent, less pages to crawl
        return null;
    }

    private Integer getTotalNumberOfPagesToVisit(String totalNumberOfPagesSelector, String mainPageHtml) {

        if (!StringUtils.isEmpty(totalNumberOfPagesSelector)) {
            Document doc = Jsoup.parse(mainPageHtml);
            Elements totalPagesElements = doc.select(totalNumberOfPagesSelector);

            int i = totalPagesElements.size() - 1;
            Integer numberPages = null;

            while (numberPages == null && i >= 0) {
                try {
                    numberPages = Integer.parseInt(totalPagesElements.get(i).text());
                } catch (NumberFormatException e) {
                    // It is not a digit
                }
                i--;
            }

            if (numberPages == null) {
                log.warn("Could not found number of pages to visit -- using maximum number: " + MAXIMUM_NUMBER_OF_PAGES);
                return MAXIMUM_NUMBER_OF_PAGES;
            } else {
                log.info("[SEARCH] We need to visit " + numberPages + " pages");
                return numberPages;
            }
        }

        log.warn("Could not found number of pages to visit -- using maximum number: " + MAXIMUM_NUMBER_OF_PAGES);

        return MAXIMUM_NUMBER_OF_PAGES;
    }

    private String getHtmlFromPageAndCache(final String link, Integer pageNumber, String siteId, TorrentSearchSiteSection sectionId, String dateWithDay, Boolean invalidate) {

        if (!invalidate) {
            return readCache(siteId, sectionId, pageNumber, link, dateWithDay);
        } else {

            Document doc = executeConnectionWithRetry(new RunnableWithResult<Document>() {
                @Override
                public Document run() {
                    try {
                        log.info("Attempting connection to link " + link);
                        Connection.Response response = Jsoup.connect(link)
                                .ignoreContentType(true)
                                .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                                .referrer("http://www.google.com")
                                .timeout(12000)
                                .followRedirects(true)
                                .execute();
                        log.info("Connection with link " + link + " was successful - starting to process");
                        return response.parse();

                    } catch (Throwable t) {
                        log.error("====> Error connecting to link " + link, t);
                        throttleThreadForMillis(1000);
                        throw new RuntimeException(t);
                    }
                }
            });

            doc.outputSettings().prettyPrint(true);

            String contentToCache = doc.outerHtml();

            if (!invalidate) {
                writeToCache(contentToCache, siteId, sectionId, pageNumber, link, dateWithDay);
            }

            return contentToCache;
        }
    }


    private void throttleThreadForMillis(long millis) {
        log.info("Throttling for " + millis + " ms");
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            // Nothing
        }
    }

    private String readCache(String siteId, TorrentSearchSiteSection sectionId, Integer pageNumber, String link, String dateWithDay) {
        try {
            File fileCache = getCacheFile(siteId, sectionId, pageNumber, link, dateWithDay);
            return FileUtils.readFileToString(fileCache);
        } catch (IOException ioe) {
            log.error("Error reading cached file", ioe);
            return null;
        }
    }

    private void writeToCache(String contentToCache, String siteId, TorrentSearchSiteSection sectionId, Integer pageNumber, String link, String dateWithDay) {
        try {
            File fileCache = getCacheFile(siteId, sectionId, pageNumber, link, dateWithDay);
            FileUtils.writeStringToFile(fileCache, contentToCache, Charset.forName("UTF-8"));
        } catch (IOException ioe) {
            log.error("Error writing cached file", ioe);
        }
    }

    private File getCacheFile(String siteId, TorrentSearchSiteSection section, Integer pageNumber, String link, String dateWithDay) {

        String param = pageNumber != null ? pageNumber + "" : link.hashCode() + "";

        String filename = crawlerBasePath + File.separator +
                siteId + File.separator +
                section.name().toLowerCase() +
                "_" + param + File.separator +
                dateWithDay + File.separator + "content.html";
        return new File(filename);
    }


    private List<TorrentSearchResult> crawlHtmlForTorrentData(TorrentWebsiteDetails websiteDetails, TorrentWebsiteSection currentSection,
                                                              TorrentContentType contentType, String singlePageHtml) {
        // List page -- contains a table with torrents
        List<TorrentSearchResult> torrentResults = extractTorrentDataFromSingleListPage(websiteDetails, currentSection,
                contentType, singlePageHtml);
        persistTorrentResults(torrentResults);
        return torrentResults;
    }

    private List<TorrentSearchResult> extractTorrentDataFromSingleListPage(TorrentWebsiteDetails websiteDetails,
                                                                           TorrentWebsiteSection currentSection,
                                                                           TorrentContentType contentType,
                                                                           String singlePageHtml) {
        Document singlePageDocument = Jsoup.parse(singlePageHtml);
        Elements elements = singlePageDocument.select(currentSection.getTorrentDataListSelector());
        List<TorrentSearchResult> torrentSearchResults = new ArrayList<>();

        // Rows of the table
        for (Element element : elements) {
            torrentSearchResults.add(extractTorrentDataFromListElement(websiteDetails, currentSection, contentType, element));
        }

        log.info("[CRAWLER] Single page - found " + torrentSearchResults.size() + " torrents");
        return torrentSearchResults;
    }

    private TorrentSearchResult extractTorrentDataFromListElement(TorrentWebsiteDetails siteDetails, TorrentWebsiteSection currentSection, TorrentContentType contentType, Element listingElement) {

        TorrentSearchResult torrentSearchResult = new TorrentSearchResult();
        torrentSearchResult.setContentType(currentSection.getContentType());
        torrentSearchResult.setLanguage(siteDetails.getLanguage());
        torrentSearchResult.setSiteId(siteDetails.getSiteId());

        if (listingElement.select(currentSection.getHeaderSelector()).isEmpty()) {

            // It is not the header
            attemptTorrentDataExtractionAndPopulateResult(siteDetails.getBaseUrl(), torrentSearchResult, currentSection,
                                                          listingElement);

            // We need to continue searching from the links chained here
            if (currentSection.getClickThroughSelectorsChain() != null) {

                getTorrentDataFromClickThroughChain(listingElement, torrentSearchResult, currentSection,
                                                    siteDetails.getBaseUrl(), siteDetails.getSiteId());
            }

            log.info("[SEARCH] - Torrent Found -- " + torrentSearchResult.getTitle() +
                     " [" + torrentSearchResult.getSeeds() + "] -- " + torrentSearchResult.getDate() + " -- " +
                     torrentSearchResult.getTorrentFileLink() + " -- " + torrentSearchResult.getMagnetLink());
        }

        return torrentSearchResult;
    }

    private void getTorrentDataFromClickThroughChain(Element baseElement, TorrentSearchResult torrentSearchResult,
                                                     TorrentWebsiteSection currentSection,
                                                     String baseUrl, String siteId) {

        String[] linkSelectorsToFollow = currentSection.getClickThroughSelectorsChain().split(";");
        Element currentElement = baseElement;

        // Visit the chain of links, using the new Document as the source of search for new data
        for (String clickThroughLinkSelector : linkSelectorsToFollow) {

            Elements clickThroughElements = currentElement.select(clickThroughLinkSelector);

            if (clickThroughElements.size() > 0) {

                Element clickThroughElement = clickThroughElements.get(0);
                String linkToVisit = clickThroughElement.attr("href");
                log.info("Clicking through link to get more information -- " + linkToVisit);

                Document newDocument = null;

                try {
                    throttleThreadForMillis(250);
                    newDocument = visitLinkElementAndGetDocumentFromHtml(clickThroughElement, baseUrl, siteId, "", true);
                } catch (Throwable t) {
                    log.info("Error visiting sub element -- " + linkToVisit, t);
                }

                attemptTorrentDataExtractionAndPopulateResult(baseUrl, torrentSearchResult, currentSection, newDocument);
                currentElement = newDocument;
            }
        }
    }

    private void attemptTorrentDataExtractionAndPopulateResult(String baseSiteUrl,
                                                               TorrentSearchResult torrentSearchResult,
                                                               TorrentWebsiteSection currentSection,
                                                               Element listingElement) {
        // Torrent title
        Elements torrentTitleElements = listingElement.select(currentSection.getTorrentTitleSelector());
        if (torrentSearchResult.getTitle() == null && torrentTitleElements.size() > 0) {
            Element torrentTitleElement = torrentTitleElements.get(0);
            String torrentTitleText = torrentTitleElement.text();
            torrentSearchResult.setTitle(torrentTitleText);
        }

        // Torrent link
        boolean isLinkSet = torrentSearchResult.getMagnetLink() != null || torrentSearchResult.getTorrentFileLink() != null;
        if (!isLinkSet && !StringUtils.isEmpty(currentSection.getTorrentFileOrMagnetLinkSelector())) {
            Elements torrentLinkElements = listingElement.select(currentSection.getTorrentFileOrMagnetLinkSelector());
            if (torrentLinkElements.size() > 0) {
                Element torrentLinkElement = torrentLinkElements.get(0);
                String torrentLink;
                if (currentSection.getLinkType() == TorrentLinkType.TORRENT_FILE) {
                    torrentLink = baseSiteUrl + torrentLinkElement.attr("href");
                    torrentSearchResult.setTorrentFileLink(torrentLink);
                } else {
                    torrentLink = torrentLinkElement.attr("href");
                    torrentSearchResult.setMagnetLink(torrentLink);
                }
            }
        }

        // Torrent date
        if (torrentSearchResult.getDate() == null && !StringUtils.isEmpty(currentSection.getDateSelector())) {
            Elements torrentDateElements = listingElement.select(currentSection.getDateSelector());
            if (torrentDateElements.size() > 0) {
                Element torrentDateElement = torrentDateElements.get(0);
                String torrentDate = torrentDateElement.text();
                LocalDateTime date = null;
                if (currentSection.getDateType() == TorrentDateRepresentationType.SPANISH_AGE) {
                    date = generateDateFromSpanishAgeRegex(torrentDate);
                } else if (currentSection.getDateType() == TorrentDateRepresentationType.FULL_DATE) {
                    date = generateDateFromFullDateUsingRegexIfPresent(torrentDate, currentSection.getDateRegex());
                } else if (currentSection.getDateType() == TorrentDateRepresentationType.ENGLISH_AGE) {
                    date = generateDateFromEnglishAgeRegex(torrentDate);
                }
                torrentSearchResult.setDate(date);
            }
        }

        // Torrent size
        Integer sizeNumber = null;
        if (torrentSearchResult.getSize() == null && !StringUtils.isEmpty(currentSection.getSizeSelector())) {
            Elements torrentSizeElements = listingElement.select(currentSection.getSizeSelector());
            if (torrentSizeElements.size() > 0) {
                Element torrentSizeElement = torrentSizeElements.get(0);
                String torrentSize = torrentSizeElement.text();
                sizeNumber = parseSize(torrentSize);
                torrentSearchResult.setSize(sizeNumber);
            }
        }

        // Torrent seeds
        Integer seedsNumber = null;
        if (torrentSearchResult.getSeeds() == null && !StringUtils.isEmpty(currentSection.getSeedsSelector())) {
            Elements seedsElements = listingElement.select(currentSection.getSeedsSelector());

            if (seedsElements.size() > 0) {
                Element seedsElement = seedsElements.get(0);
                String seeds = seedsElement.text();

                try {
                    seedsNumber = Integer.parseInt(seeds);
                } catch (NumberFormatException e) {
                    seedsNumber = null;
                }
            }
        }

        torrentSearchResult.setSeeds(seedsNumber);
    }

    private LocalDateTime generateDateFromSpanishAgeRegex(String rawDateText) {

        String spanishAgeRegex = "Hace\\s+((?:[0-9]+)|(?:un)|(?:una))\\s+((?:hr)|(?:día)|(?:sem)|(?:mes)|(?:año))";
        Pattern spanishAgePattern = Pattern.compile(spanishAgeRegex);
        Matcher matcher = spanishAgePattern.matcher(rawDateText);

        if (matcher.find()) {

            LocalDateTime dateTime = new LocalDateTime();
            dateTime.withTime(0, 0, 0, 0);
            String number = matcher.group(1);
            String timeRepresentation = matcher.group(2);

            if (number.contains("un")) {
                number = "1";
            }

            switch (timeRepresentation) {
                case "hr":
                    dateTime = dateTime.minusHours(Integer.parseInt(number));
                    break;
                case "día":
                    dateTime = dateTime.minusDays(Integer.parseInt(number));
                    break;
                case "sem":
                    dateTime = dateTime.minusWeeks(Integer.parseInt(number));
                    break;
                case "mes":
                    dateTime = dateTime.minusMonths(Integer.parseInt(number));
                    break;
                case "año":
                    dateTime = dateTime.minusYears(Integer.parseInt(number));
                    break;
                default:
                    log.error("Time representation not considered yet -- " + timeRepresentation);
                    throw new IllegalArgumentException("Time representation not considered yet -- " + timeRepresentation);
            }

            return dateTime;

        } else {
            log.warn("No date information found for raw date text " + rawDateText + " following regex " + spanishAgeRegex);
            return null;
        }
    }

    private LocalDateTime generateDateFromEnglishAgeRegex(String rawDateText) {
        String englishAgeRegex = "\\s*([0-9]+)(?:.*)(min|hour|day|week|month|year)";
        Pattern englishAgePattern = Pattern.compile(englishAgeRegex);
        Matcher matcher = englishAgePattern.matcher(rawDateText);

        if (matcher.find()) {

            String numberStr = matcher.group(1);
            String timeRepresentation = matcher.group(2);

            Integer number = Integer.parseInt(numberStr.trim());
            LocalDateTime date = new LocalDateTime();
            date = date.withTime(0, 0, 0, 0);

            switch (timeRepresentation) {
                case "min":
                    date = date.minusMinutes(number);
                    break;
                case "hour":
                    date = date.minusHours(number);
                    break;
                case "day":
                    date = date.minusDays(number);
                    break;
                case "week":
                    date = date.minusWeeks(number);
                    break;
                case "month":
                    date = date.minusMonths(number);
                    break;
                case "year":
                    date = date.minusMonths(number);
                    break;
                default:
                    log.error("Time representation not considered yet -- " + timeRepresentation);
                    throw new IllegalArgumentException("Time representation not considered yet -- " + timeRepresentation);
            }

            return date;
        } else {
            log.warn("No date information found for raw date text " + rawDateText + " following regex " + englishAgeRegex);
            return null;
        }
    }


    private Integer parseSize(String rawSize) {
        Pattern torrentSizePattern = Pattern.compile(TORRENT_SIZE_REGEX);
        Matcher matcher = torrentSizePattern.matcher(rawSize);
        if (matcher.find()) {

            String sizeStr = matcher.group(1);
            String unitStr = matcher.group(2);

            Double sizeDouble = Double.parseDouble(sizeStr);
            Integer size;

            if (unitStr.toLowerCase().equals("gb")) {
                size = new Double(sizeDouble * 1024).intValue();
            } else {
                size = new Double(sizeDouble).intValue();
            }

            return size;
        } else {
            log.warn("No size information found for raw date text " + rawSize + " following regex " + TORRENT_SIZE_REGEX);
            return null;
        }
    }

    public <T> T executeConnectionWithRetry(RunnableWithResult<T> runnable) {

        boolean success = false;
        int retryCount = 0;
        Throwable exception = null;
        T result = null;

        while (!success && retryCount < CONNECTION_RETRY_COUNT) {
            try {
                result = runnable.run();
                success = true;
            } catch (Throwable e) {
                retryCount++;
                exception = e;
                log.warn("Connection failing -- retrying " + retryCount, exception);
                throttleThreadForMillis(PAUSE_TIME_BETWEEN_TRIES);
            }
        }

        if (!success) {
            log.error("Connection failed after " + CONNECTION_RETRY_COUNT + " calls -- giving up ", exception);
            throw new RuntimeException(exception);
        } else {
            return result;
        }
    }

    public List<Future<?>> startCrawlers() {

        Future<?> future1 = executorService.submit(new Runnable() {
            @Override
            public void run() {
                crawlMejorTorrent();
            }
        });

        Future<?> future2 = executorService.submit(new Runnable() {
            @Override
            public void run() {
                crawlEliteTorrent();
            }
        });

        List<Future<?>> futures = new ArrayList<>();
        futures.add(future1);
        futures.add(future2);

        return futures;
    }

    private void persistTorrentResults(List<TorrentSearchResult> searchResults) {

        List<TorrentSearchResult> resultsToPersist = new ArrayList<>(searchResults);

        int persisted = 0;

        for (TorrentSearchResult searchResult : resultsToPersist) {
            String hash = calculateHashForSearchResult(searchResult);

            if (searchResult.getMagnetLink() != null || searchResult.getTorrentFileLink() != null) {

               // if (!torrentService.existsResultWithHash(hash) && searchResult.getId() == null) {
                    searchResult.setSearchHash(hash);
                    final TorrentSearchResult finalSearchResult = searchResult;

                    try {

//                        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//                            @Override
//                            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
//                                torrentService.persistTorrentSearchResult(finalSearchResult);
//                            }
//                        });

                        //TODO: persist

                        persisted++;

                        log.info("[PERSIST-RESULT] Persisted torrent -- " + finalSearchResult.getTitle());

                    } catch (Throwable t) {
                        log.error("Error inserting torrents in DB -- ", t);
                    }
                }
            }
        //}

        //log.info("[PERSIST-RESULT] Persisted " + persisted + " torrents");
    }

    private String calculateHashForSearchResult(TorrentSearchResult searchResult) {

        String toHash = searchResult.getMagnetLink() != null ? searchResult.getMagnetLink() : "";
        toHash += searchResult.getTorrentFileLink() != null ? searchResult.getTorrentFileLink() : "";
        String hash = DigestUtils.md5Hex(toHash.getBytes());
        return hash;
    }

    private interface RunnableWithResult<T> {
        T run();
    }

    private String extractTitleFromMagnetLink(String magnetLink) {

        try {
            Pattern magnetPattern = Pattern.compile(TITLE_IN_MAGNET_LINK_REGEX);
            Matcher magnetMatcher = magnetPattern.matcher(magnetLink);
            if (magnetMatcher.find()) {
                String titleEncoded = magnetMatcher.group(1);
                return URLDecoder.decode(titleEncoded, "UTF-8");
            }

        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported encoding extracting title from magnet link ", e);
        }

        return null;
    }

    private LocalDateTime generateDateFromFullDateUsingRegexIfPresent(String rawDateText, String dateRegex) {

        try {
            //TODO: Put this in preferences of the website
            DateTimeFormatter dt = DateTimeFormat.forPattern("dd-MM-yyyy");
            if (dateRegex != null) {

                Pattern datePattern = Pattern.compile(dateRegex);
                Matcher dateMatcher = datePattern.matcher(rawDateText);

                if (dateMatcher.find()) {
                    String dateString = dateMatcher.group(1);
                    return LocalDateTime.parse(dateString.trim(), dt);
                }

            } else {
                return LocalDateTime.parse(rawDateText, dt);
            }

        } catch (Throwable t) {
            log.warn("Error extracting date from text: " + rawDateText + " - Regex " + dateRegex);
        }
        return null;

    }

    public static void main(String[] args) {
        TorrentSearchService torrentSearch = new TorrentSearchService();
        String title;
        torrentSearch.extractTitleFromTorrentFile(
                "http://www.mejortorrent.com//uploads/torrents/series/Alfred_1x09_No.vuelvas.torrent");
        String magnet = "magnet:?xt=urn:btih:ojokteksmbk46cu2ebemw4mkhhdbdaya&dn=Momentum+%28HDRip%29+%28EliteTorrent.net%29&tr=http://tracker.torrentbay.to:6969/announce";
        title = torrentSearch.extractTitleFromMagnetLink(magnet);
        System.out.println("Title " + title);
    }

}

