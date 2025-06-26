INSERT INTO theme(name, description, thumbnail)
VALUES ('추리', '셜록 with Danny', 'image/thumbnail.png'),
       ('공포', '어둠 속의 비명', 'image/thumbnail.png'),
       ('모험', '잃어버린 도시', 'image/thumbnail.png'),
       ('SF', '우주 탈출 미션', 'image/thumbnail.png'),
       ('감성', '시간을 걷는 집', 'image/thumbnail.png'),
       ('판타지', '마법사의 유산', 'image/thumbnail.png'),
       ('역사', '고려 왕실의 비밀', 'image/thumbnail.png'),
       ('범죄', '은행 강도 사건', 'image/thumbnail.png'),
       ('스릴러', '잠입 작전', 'image/thumbnail.png'),
       ('코미디', '웃음 연구소', 'image/thumbnail.png'),
       ('로맨스', '잃어버린 편지', 'image/thumbnail.png'),
       ('논리', '퍼즐 마스터', 'image/thumbnail.png');

-- 예약 시간 데이터
INSERT INTO reservation_time(start_at)
VALUES ('08:00'),
       ('12:00'),
       ('14:00'),
       ('16:00'),
       ('18:00');

INSERT INTO member(name, email, password, role)
VALUES ('admin', 'admin@gmail.com', 'password', 'ADMIN'),
       ('mint', 'mint@gmail.com', 'password', 'REGULAR'),
       ('aina', 'aina@gmail.com', 'password', 'REGULAR');

INSERT INTO reservation(date, theme_id, time_id, member_id)
VALUES ('2025-06-20', 3L, 1L, 1L),
       ('2025-06-20', 5L, 1L, 1L),
       ('2025-06-20', 5L, 2L, 1L)
;
