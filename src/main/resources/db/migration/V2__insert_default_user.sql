-- Insert default user: Oldp1e
-- Email: oldp1e@sfp.com
-- Password: oldp1e@2025
-- BCrypt hash (strength 10) - validado com BCryptPasswordEncoder
INSERT INTO users (id, email, password_hash, full_name, is_active, role, created_at, updated_at, last_login_at)
VALUES (
    RANDOM_UUID(),
    'oldp1e@sfp.com',
    '${DB_USER_PASSWORD_HASH}',
    'Oldp1e',
    TRUE,
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);
