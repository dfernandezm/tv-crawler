package com.morenware.tvcrawler.persistence.domain;

/**
 * Created by david on 17/05/15.
 */
public enum TorrentState {

   DOWNLOADING, DOWNLOAD_COMPLETED, RENAMING, RENAMING_COMPLETED, FETCHING_SUBTITLES, FAILED_DOWNLOAD_ATTEMPT,
   NEW, AWAITING_DOWNLOAD, COMPLETED_WITH_ERROR;

}
