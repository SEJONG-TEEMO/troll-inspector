CREATE TABLE summoner
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(255)          NOT NULL,
    tag          VARCHAR(255)          NOT NULL,
    tier         VARCHAR(20)           NOT NULL,
    league_point BIGINT                NOT NULL,
    level        INT                   NOT NULL,
    wins         INT                   NOT NULL,
    losses       INT                   NOT NULL,
    create_at    datetime              NOT NULL,
    update_at    datetime              NOT NULL,
    CONSTRAINT pk_summoner PRIMARY KEY (id)
);

CREATE INDEX name_tag_idx ON summoner (name, tag);