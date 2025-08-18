package kr.co.shortenurlservice.infrastructure;

import kr.co.shortenurlservice.domain.ShortenUrl;
import kr.co.shortenurlservice.domain.ShortenUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ShortenUrlRepositoryImpl implements ShortenUrlRepository {

    private final JpaShortenUrlRepository jpaShortenUrlRepository;
    private final ConcurrentHashMap<String, ShortenUrl> cache;
    private final ConcurrentHashMap<String, AtomicInteger> redirectCountMap;

    @Autowired
    public ShortenUrlRepositoryImpl(JpaShortenUrlRepository jpaShortenUrlRepository) {
        this.jpaShortenUrlRepository = jpaShortenUrlRepository;
        this.cache = new ConcurrentHashMap<>();
        this.redirectCountMap = new ConcurrentHashMap<>();
    }

    @Override
    public void saveShortenUrl(ShortenUrl shortenUrl) {
        jpaShortenUrlRepository.save(shortenUrl);
        cache.put(shortenUrl.getShortenUrlKey(), shortenUrl);
    }

    @Async
    @Override
    public void asyncSaveShortenUrl(ShortenUrl shortenUrl) {
        jpaShortenUrlRepository.save(shortenUrl);
        cache.put(shortenUrl.getShortenUrlKey(), shortenUrl);
    }

    @Override
    public void increaseRedirectCount(ShortenUrl shortenUrl) {
        String key = shortenUrl.getShortenUrlKey();
        redirectCountMap.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey) {
        // 먼저 캐시에서 조회
        ShortenUrl shortenUrl = cache.get(shortenUrlKey);

        if (shortenUrl == null) {
            // 캐시에 없으면 데이터베이스에서 조회
            shortenUrl = jpaShortenUrlRepository.findByShortenUrlKey(shortenUrlKey);
            if (shortenUrl != null) {
                // 데이터베이스에 있으면 캐시에 저장
                cache.put(shortenUrlKey, shortenUrl);
            }
        }

        return shortenUrl;
    }

    @Scheduled(fixedRate = 10000)
    @Override
    public void updateRedirectCounts() {
        redirectCountMap.forEach((key, count) -> {
            int increment = count.getAndSet(0);
            if (increment > 0) {
                jpaShortenUrlRepository.incrementRedirectCount(key, increment);
            }
        });
    }
}
