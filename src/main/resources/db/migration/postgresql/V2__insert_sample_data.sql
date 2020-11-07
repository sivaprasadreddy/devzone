
INSERT INTO roles (id, name, created_at) VALUES
(1, 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(2, 'ROLE_MODERATOR', CURRENT_TIMESTAMP),
(3, 'ROLE_USER', CURRENT_TIMESTAMP)
;

INSERT INTO users (email, password, name, created_at) VALUES
('siva@gmail.com', '$2a$10$CIXGKN9rPfV/mmBMYas.SemoT9mfVUUwUxueFpU3DcWhuNo5fexYC', 'Siva', CURRENT_TIMESTAMP)
;

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(1, 3)
;

INSERT INTO tags(name) VALUES
('java'),
('spring'),
('spring-boot'),
('spring-cloud'),
('jpa'),
('hibernate'),
('junit'),
('devops'),
('maven'),
('gradle'),
('security')
;
