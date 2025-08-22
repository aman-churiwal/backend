package viteezy.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Review {

    private final Long id;
    private final String source;
    private final Integer total;
    private final Integer min_score;
    private final Integer max_score;
    private final BigDecimal score;
    private final String scoreLabel;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public Review(Long id, String source, Integer total, Integer min_score, Integer max_score, BigDecimal score,
                  String scoreLabel, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.source = source;
        this.total = total;
        this.min_score = min_score;
        this.max_score = max_score;
        this.score = score;
        this.scoreLabel = scoreLabel;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getMin_score() {
        return min_score;
    }

    public Integer getMax_score() {
        return max_score;
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getScoreLabel() {
        return scoreLabel;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }
}
