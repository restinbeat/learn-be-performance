package kr.co.shortenurlservice.infrastructure;

import kr.co.shortenurlservice.domain.ShortenUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface JpaShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {
    ShortenUrl findByShortenUrlKey(String shortenUrlKey);

    @Modifying
    @Transactional
    @Query("UPDATE shorten_url SET shorten_url.redirectCount = shorten_url.redirectCount + 1 WHERE shorten_url.shortenUrlKey = :shortenUrlKey")
    int incrementRedirectCount(String shortenUrlKey);
}
