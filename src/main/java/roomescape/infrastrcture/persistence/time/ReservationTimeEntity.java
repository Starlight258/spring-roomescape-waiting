package roomescape.infrastrcture.persistence.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.time.ReservationTime;

@Entity
@Table(name = "reservation_times")
public class ReservationTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 기본 생성자 (JPA 필수)
    protected ReservationTimeEntity() {
    }

    // 생성자
    public ReservationTimeEntity(LocalTime startTime) {
        this.startTime = startTime;
        this.createdAt = LocalDateTime.now();
    }

    // ID 포함 생성자 (restore용)
    public ReservationTimeEntity(Long id, LocalTime startTime, LocalDateTime createdAt) {
        this.id = id;
        this.startTime = startTime;
        this.createdAt = createdAt;
    }

    // ✅ 도메인 객체로 변환
    public ReservationTime toDomain() {
        return ReservationTime.restore(id, startTime, createdAt);
    }

    // ✅ 도메인 객체에서 생성
    public static ReservationTimeEntity from(ReservationTime reservationTime) {
        if (reservationTime.getId() != null) {
            // ID가 있는 경우 (이미 저장된 시간)
            return new ReservationTimeEntity(
                    reservationTime.getId(),
                    reservationTime.getStartTime(),
                    reservationTime.getCreatedAt()
            );
        } else {
            // ID가 없는 경우 (새로 생성하는 시간)
            return new ReservationTimeEntity(reservationTime.getStartTime());
        }
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

