package kr.co.shortenurlservice.infrastructure;

import kr.co.shortenurlservice.domain.ShortenUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JpaShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {
    ShortenUrl findByShortenUrlKey(String shortenUrlKey);

    @Modifying
    @Transactional
    @Query("UPDATE ShortenUrl su SET su.redirectCount = su.redirectCount + :increment WHERE su.shortenUrlKey = :shortenUrlKey")
    int incrementRedirectCount(@Param("shortenUrlKey") String shortenUrlKey, @Param("increment") int increment);
}
