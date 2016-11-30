package com.morenware.tvcrawler.service;

/**
 * Created by david on 17/05/15.
 */
public class TorrentService {

//    @Resource(name = "torrentDao")
//    private HibernateDao<Torrent,Integer> torrentDao;
//
//    @Resource(name = "torrentSearchResultDao")
//    private HibernateDao<TorrentSearchResult,Integer> torrentSearchResultDao;
//
//    @Transactional
//    public void createTorrent() {
//        Torrent torrent = new Torrent();
//        torrent.setGuid("7676sss");
//        torrent.setTitle("Title");
//        torrent.setTorrentName("SometorrentName");
//        torrent.setPercentDone(100D);
//        torrent.setContentType(TorrentContentType.TV_SHOW);
//        torrentDao.persist(torrent);
//    }
//
//    @Transactional
//    public void deleteTorrent(Integer id) {
//        torrentDao.remove(id);
//    }
//
//    @Transactional(readOnly = true)
//    public boolean  existsResultWithHash(String hash) {
//        Query query = torrentSearchResultDao.createNamedQuery("TorrentSearchResult.findByHash");
//        query.setParameter("searchHash", hash);
//        List<TorrentSearchResult> searchResults = (List<TorrentSearchResult>) query.list();
//        return searchResults.size() > 0;
//    }
//
//    @Transactional
//    public void persistTorrentSearchResult(TorrentSearchResult torrentSearchResult) {
//        torrentSearchResultDao.persist(torrentSearchResult);
//    }

}
