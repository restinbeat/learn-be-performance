package kr.co.shortenurlservice.domain;

public interface ShortenUrlRepository {
    void saveShortenUrl(ShortenUrl shortenUrl);
    void asyncSaveShortenUrl(ShortenUrl shortenUrl);
    void increaseRedirectCount(ShortenUrl shortenUrl);
    ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey);
}
