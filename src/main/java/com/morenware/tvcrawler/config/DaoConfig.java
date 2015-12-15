package com.morenware.tvcrawler.config;

import com.morenware.tvcrawler.persistence.dao.HibernateDaoImpl;
import com.morenware.tvcrawler.persistence.domain.Torrent;
import com.morenware.tvcrawler.persistence.domain.TorrentSearchResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

	@Bean
	public HibernateDaoImpl<Torrent, Integer> torrentDao() {
		HibernateDaoImpl<Torrent, Integer> torrentDao = new HibernateDaoImpl<>();
		torrentDao.setEntityClass(Torrent.class);
		return torrentDao;
	}

	@Bean
	public HibernateDaoImpl<TorrentSearchResult, Integer> torrentSearchResultDao() {
		HibernateDaoImpl<TorrentSearchResult, Integer> torrentSearchResultDao = new HibernateDaoImpl<>();
		torrentSearchResultDao.setEntityClass(TorrentSearchResult.class);
		return torrentSearchResultDao;
	}

}
