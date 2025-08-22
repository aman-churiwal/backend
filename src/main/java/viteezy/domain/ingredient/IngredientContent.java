package viteezy.domain.ingredient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientContent {

    private final Long id;
    private final Long ingredientId;
    private final String description;
    private final String bullets;
    private final String title1;
    private final String text1;
    private final String title2;
    private final String text2;
    private final String title3;
    private final String text3;
    private final String notice;
    private final List<IngredientArticle> articles;
    private final String excipients;
    private final String claim;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public IngredientContent(Long id, Long ingredientId, String description, String bullets, String title1, String text1,
                             String title2, String text2, String title3, String text3, String notice,
                             List<IngredientArticle> articles, String excipients, String claim,
                             LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.description = description;
        this.bullets = bullets;
        this.title1 = title1;
        this.text1 = text1;
        this.title2 = title2;
        this.text2 = text2;
        this.title3 = title3;
        this.text3 = text3;
        this.notice = notice;
        this.articles = articles;
        this.excipients = excipients;
        this.claim = claim;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public String getDescription() {
        return description;
    }

    public String getBullets() {
        return bullets;
    }

    public String getTitle1() {
        return title1;
    }

    public String getText1() {
        return text1;
    }

    public String getTitle2() {
        return title2;
    }

    public String getText2() {
        return text2;
    }

    public String getTitle3() {
        return title3;
    }

    public String getText3() {
        return text3;
    }

    public String getNotice() {
        return notice;
    }

    public List<IngredientArticle> getArticles() {
        return articles;
    }

    public String getExcipients() {
        return excipients;
    }

    public String getClaim() {
        return claim;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientContent that = (IngredientContent) o;
        return Objects.equals(id, that.id) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(description, that.description) && Objects.equals(bullets, that.bullets) && Objects.equals(title1, that.title1) && Objects.equals(text1, that.text1) && Objects.equals(title2, that.title2) && Objects.equals(text2, that.text2) && Objects.equals(title3, that.title3) && Objects.equals(text3, that.text3) && Objects.equals(notice, that.notice) && Objects.equals(articles, that.articles) && Objects.equals(excipients, that.excipients) && Objects.equals(claim, that.claim) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, description, bullets, title1, text1, title2, text2, title3, text3, notice, articles, excipients, claim, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientContent.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("description='" + description + "'")
                .add("bullets='" + bullets + "'")
                .add("title1='" + title1 + "'")
                .add("text1='" + text1 + "'")
                .add("title2='" + title2 + "'")
                .add("text2='" + text2 + "'")
                .add("title3='" + title3 + "'")
                .add("text3='" + text3 + "'")
                .add("notice='" + notice + "'")
                .add("articles=" + articles)
                .add("excipients='" + excipients + "'")
                .add("claim='" + claim + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
