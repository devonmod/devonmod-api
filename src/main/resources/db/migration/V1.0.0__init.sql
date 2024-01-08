CREATE TABLE IF NOT EXISTS devonmod_user
(
    id            uuid         NOT NULL,
    name          VARCHAR(32)  NOT NULL,
    surname       VARCHAR(32)  NOT NULL,
    username      VARCHAR(32)  NOT NULL,
    email         VARCHAR(512) NOT NULL,
    user_role     VARCHAR(32)  NOT NULL,
    password      VARCHAR(512) NOT NULL,
    refresh_token VARCHAR(1024),
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP,
    PRIMARY KEY (id)
    );

INSERT INTO devonmod_user (id, name, surname, username, email, user_role, password, refresh_token,
                      created_at, updated_at)
VALUES ('c7f73c6f-8de6-4da7-af17-985580331600', 'Admin', 'Devonmod', 'devonmod',
        'devonmod@gmail.com', 'ADMIN',
        '{bcrypt}$2a$10$7XgCiRxUtwhIp.2EmAVcgeLk/xk82OUv6hpv8tYya5d48pnjWwhB2',
        NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);