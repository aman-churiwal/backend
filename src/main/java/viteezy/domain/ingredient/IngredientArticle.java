package viteezy.domain.ingredient;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IngredientArticle {

    private final Long id;
    private final Long ingredientId;
    private final String author;
    private final String title;
    private final String url;
    private final String source;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public IngredientArticle(Long id, Long ingredientId, String author, String title, String url, String source,
                             LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.author = author;
        this.title = title;
        this.url = url;
        this.source = source;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
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
        IngredientArticle that = (IngredientArticle) o;
        return Objects.equals(id, that.id) && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(author, that.author) && Objects.equals(title, that.title) && Objects.equals(url, that.url) && Objects.equals(source, that.source) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, author, title, url, source, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientArticle.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ingredientId=" + ingredientId)
                .add("author='" + author + "'")
                .add("title='" + title + "'")
                .add("url='" + url + "'")
                .add("source='" + source + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
