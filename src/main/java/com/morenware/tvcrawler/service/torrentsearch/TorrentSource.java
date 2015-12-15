package com.morenware.tvcrawler.service.torrentsearch;

import com.morenware.tvcrawler.persistence.domain.TorrentSearchResult;

import java.util.List;

/**
 * Created by david on 26/05/15.
 */
public interface TorrentSource {

    List<TorrentSearchResult> getLatestMovies();
    List<TorrentSearchResult> getLatestTvShows();
    List<TorrentSearchResult> getAllTorrentData();
}
