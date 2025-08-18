package kr.co.shortenurlservice.domain;

import org.springframework.scheduling.annotation.Scheduled;

public interface ShortenUrlRepository {
    void saveShortenUrl(ShortenUrl shortenUrl);
    void asyncSaveShortenUrl(ShortenUrl shortenUrl);
    void increaseRedirectCount(ShortenUrl shortenUrl);
    ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey);

    @Scheduled(fixedRate = 10000)
    void updateRedirectCounts();
}
