package roomescape.infrastrcture.persistence.reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationStatus;
import roomescape.infrastrcture.persistence.member.MemberEntity;
import roomescape.infrastrcture.persistence.theme.ThemeEntity;
import roomescape.infrastrcture.persistence.time.ReservationTimeEntity;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_time_id")
    private ReservationTimeEntity reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private ThemeEntity theme;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime createdAt;

    // 기본 생성자
    protected ReservationEntity() {
    }

    // 도메인 객체로 변환
    public Reservation toDomain() {
        return Reservation.restore(
                id,
                member.toDomain(),
                new ReservationDate(date),
                reservationTime.toDomain(),
                theme.toDomain(),
                status,
                createdAt
        );
    }

    // 도메인 객체에서 생성
    public static ReservationEntity from(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.member = MemberEntity.from(reservation.getMember());
        entity.date = reservation.getDate().getDate();
        entity.reservationTime = ReservationTimeEntity.from(reservation.getTime());
        entity.theme = ThemeEntity.from(reservation.getTheme());
        entity.status = reservation.getStatus();
        entity.createdAt = reservation.getCreatedAt();
        return entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(final MemberEntity member) {
        this.member = member;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public ReservationTimeEntity getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(final ReservationTimeEntity reservationTime) {
        this.reservationTime = reservationTime;
    }

    public ThemeEntity getTheme() {
        return theme;
    }

    public void setTheme(final ThemeEntity theme) {
        this.theme = theme;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(final ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
