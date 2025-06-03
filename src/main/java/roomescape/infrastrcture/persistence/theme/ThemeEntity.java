package roomescape.infrastrcture.persistence.theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThumbnailImage;

@Entity
@Table(name = "themes")
public class ThemeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 기본 생성자 (JPA 필수)
    protected ThemeEntity() {
    }

    // 생성자
    public ThemeEntity(String name, String description, String thumbnailUrl) {
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = LocalDateTime.now();
    }

    // ID 포함 생성자 (restore용)
    public ThemeEntity(Long id, String name, String description,
                       String thumbnailUrl, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    // ✅ 도메인 객체로 변환
    public Theme toDomain() {
        return Theme.restore(
                id,
                new ThemeName(name),
                new ThemeDescription(description),
                new ThumbnailImage(thumbnailUrl),
                createdAt
        );
    }

    // ✅ 도메인 객체에서 생성
    public static ThemeEntity from(Theme theme) {
        if (theme.getId() != null) {
            // ID가 있는 경우 (이미 저장된 테마)
            return new ThemeEntity(
                    theme.getId(),
                    theme.getName().getValue(),
                    theme.getDescription().getValue(),
                    theme.getThumbnail().getUrl(),
                    theme.getCreatedAt()
            );
        } else {
            // ID가 없는 경우 (새로 생성하는 테마)
            return new ThemeEntity(
                    theme.getName().getValue(),
                    theme.getDescription().getValue(),
                    theme.getThumbnail().getUrl()
            );
        }
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

