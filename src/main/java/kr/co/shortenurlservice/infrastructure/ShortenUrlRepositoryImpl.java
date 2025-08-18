package kr.co.shortenurlservice.infrastructure;

import kr.co.shortenurlservice.domain.ShortenUrl;
import kr.co.shortenurlservice.domain.ShortenUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class ShortenUrlRepositoryImpl implements ShortenUrlRepository {

    private final JpaShortenUrlRepository jpaShortenUrlRepository;
    private final RedisTemplate<String, ShortenUrl> redisTemplate;
    private static final String CACHE_PREFIX = "shortenUrl::";

    @Autowired
    public ShortenUrlRepositoryImpl(JpaShortenUrlRepository jpaShortenUrlRepository,
                                    RedisTemplate<String, ShortenUrl> redisTemplate) {
        this.jpaShortenUrlRepository = jpaShortenUrlRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveShortenUrl(ShortenUrl shortenUrl) {
        jpaShortenUrlRepository.save(shortenUrl);
        redisTemplate.opsForValue().set(CACHE_PREFIX + shortenUrl.getShortenUrlKey(), shortenUrl);
    }

    @Async
    @Override
    public void asyncSaveShortenUrl(ShortenUrl shortenUrl) {
        jpaShortenUrlRepository.save(shortenUrl);
        redisTemplate.opsForValue().set(CACHE_PREFIX + shortenUrl.getShortenUrlKey(), shortenUrl);
    }

    @Override
    public void increaseRedirectCount(ShortenUrl shortenUrl) {
        jpaShortenUrlRepository.incrementRedirectCount(shortenUrl.getShortenUrlKey());
    }

    @Override
    public ShortenUrl findShortenUrlByShortenUrlKey(String shortenUrlKey) {
        // 먼저 Redis 캐시에서 조회
        ShortenUrl shortenUrl = redisTemplate.opsForValue().get(CACHE_PREFIX + shortenUrlKey);

        if (shortenUrl == null) {
            // 캐시에 없으면 데이터베이스에서 조회
            shortenUrl = jpaShortenUrlRepository.findByShortenUrlKey(shortenUrlKey);
            if (shortenUrl != null) {
                // 데이터베이스에 있으면 캐시에 저장
                redisTemplate.opsForValue().set(CACHE_PREFIX + shortenUrlKey, shortenUrl);
            }
        }

        return shortenUrl;
    }

}
