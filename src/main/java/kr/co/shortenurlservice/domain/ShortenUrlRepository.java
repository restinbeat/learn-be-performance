package kr.co.shortenurlservice.domain;

public interface ShortenUrlRepository {
    void saveShortenUrl(ShortenUrl shortenUrl);
    void asyncSaveShortenUrl(ShortenUrl shortenUrl);
    ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey);
}
