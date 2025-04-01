-- Добавление ролей
INSERT INTO roles (id, role_name)
VALUES (1, 'ROLE_ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (id, role_name)
VALUES (2, 'ROLE_USER')
ON CONFLICT (id) DO NOTHING;

-- Добавление пользователей
INSERT INTO users (id, email, login, password)
VALUES (1, 'admin@test.ru', 'admin', '$2a$12$xfuHoy/Dw43Y6xUjorEl0ubUHyxO4EfAkeqi7YhcH1FGjfG.kKfJO'),
       (2, 'user@test.ru', 'user', '$2a$12$xfuHoy/Dw43Y6xUjorEl0ubUHyxO4EfAkeqi7YhcH1FGjfG.kKfJO')
ON CONFLICT (id) DO NOTHING;

-- Привязка ролей к админу
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1) ON CONFLICT DO NOTHING; -- admin → ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2) ON CONFLICT DO NOTHING; -- admin → ROLE_USER

-- Привязка роли к пользователю
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2) ON CONFLICT DO NOTHING; -- user → ROLE_USER