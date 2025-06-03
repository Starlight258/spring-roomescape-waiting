-- data.sql
-- 관리자 계정 생성 (비밀번호: admin123)
INSERT INTO members (email, password, name, role, created_at)
VALUES ('admin@roomescape.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '관리자', 'ADMIN', NOW());

-- 기본 테마들
INSERT INTO themes (name, description, thumbnail_url, created_at)
VALUES ('추리', '셜록홈즈와 함께하는 추리 게임', '/images/mystery.jpg', NOW()),
       ('공포', '어둠 속의 공포 체험', '/images/horror.jpg', NOW()),
       ('SF', '미래 세계 탈출 모험', '/images/scifi.jpg', NOW());

-- 기본 예약 시간들 (9시~22시, 30분 간격)
INSERT INTO reservation_times (start_time, created_at)
VALUES ('09:00', NOW()),
       ('09:30', NOW()),
       ('10:00', NOW()),
       ('10:30', NOW()),
       ('11:00', NOW()),
       ('11:30', NOW()),
       ('12:00', NOW()),
       ('12:30', NOW()),
       ('13:00', NOW()),
       ('13:30', NOW()),
       ('14:00', NOW()),
       ('14:30', NOW()),
       ('15:00', NOW()),
       ('15:30', NOW()),
       ('16:00', NOW()),
       ('16:30', NOW()),
       ('17:00', NOW()),
       ('17:30', NOW()),
       ('18:00', NOW()),
       ('18:30', NOW()),
       ('19:00', NOW()),
       ('19:30', NOW()),
       ('20:00', NOW()),
       ('20:30', NOW()),
       ('21:00', NOW()),
       ('21:30', NOW()),
       ('22:00', NOW());
