package com.morenware.tvcrawler.service;

import com.google.inject.Inject;
import com.morenware.tvcrawler.persistence.domain.Torrent;
import com.sun.tools.javac.util.List;

/**
 * Created by davidfernandez on 02/12/2016.
 */
public class SearchService {

	private TorrentService torrentService;

	@Inject
	public SearchService(TorrentService torrentService) {
		this.torrentService = torrentService;
	}

	public List<Torrent> search(String query) {
		return null;
	}
}
