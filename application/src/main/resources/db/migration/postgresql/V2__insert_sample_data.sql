INSERT INTO users (email, password, name, role, created_at) VALUES
('admin@gmail.com', '$2a$10$ZuGgeoawgOg.6AM3QEGZ3O4QlBSWyRx3A70oIcBjYPpUB8mAZWY16', 'Admin', 'ROLE_ADMIN', CURRENT_TIMESTAMP),
('siva@gmail.com', '$2a$10$CIXGKN9rPfV/mmBMYas.SemoT9mfVUUwUxueFpU3DcWhuNo5fexYC', 'Siva', 'ROLE_MODERATOR', CURRENT_TIMESTAMP),
('user@gmail.com', '$2a$10$9.asbEZnVSA24cavY2xStO1FQS54WZnxUzSxqYepEoCFYAvUVnVr6', 'DemoUser', 'ROLE_USER', CURRENT_TIMESTAMP)
;

INSERT INTO categories(name, created_at) VALUES
('java', CURRENT_TIMESTAMP),
('spring', CURRENT_TIMESTAMP),
('spring-boot', CURRENT_TIMESTAMP),
('spring-cloud', CURRENT_TIMESTAMP),
('jpa', CURRENT_TIMESTAMP),
('hibernate', CURRENT_TIMESTAMP),
('junit', CURRENT_TIMESTAMP),
('devops', CURRENT_TIMESTAMP),
('maven', CURRENT_TIMESTAMP),
('gradle', CURRENT_TIMESTAMP),
('security', CURRENT_TIMESTAMP)
;
