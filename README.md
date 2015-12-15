# TV Crawler

Spring Boot application to crawl websites to search for Movies and Tv Shows links.

Websites supported:

* EliteTorrent.net
* DivxTotal.com
* MejorTorrent.com

The aim is to crawl the websites periodically, extract links and data and populate a document-based datastore. This datastore can then be queried/consumed by a third party app. This could also be done by developing an API to consume the datastore through this application.

Right now the fetched data is stored in a MySQL database using Hibernate, but future additions contemplate:

* ElasticSearch index
* JSON files in filesystem
* MongoDB
* Embedded database

Still very rough application, very inefficient threading and disorganised code.
