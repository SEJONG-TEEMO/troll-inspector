CREATE TEMPORARY TABLE user_info
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    game_name       VARCHAR(255)          NOT NULL,
    tag_line        VARCHAR(255)          NOT NULL,
    puuid           VARCHAR(100)          NOT NULL,
    summoner_id     VARCHAR(100)          NOT NULL,
    queue_type      VARCHAR(255)          NOT NULL,
    tier            VARCHAR(20)           NOT NULL,
    `rank`          VARCHAR(255)          NOT NULL,
    wins            INT                   NOT NULL,
    losses          INT                   NOT NULL,
    league_point    INT                   NOT NULL,
    account_id      VARCHAR(100)          NOT NULL,
    profile_icon_id INT                   NOT NULL,
    revision_data   BIGINT                NOT NULL,
    summoner_level  BIGINT                NOT NULL,
    CONSTRAINT pk_user_info PRIMARY KEY (id)
);