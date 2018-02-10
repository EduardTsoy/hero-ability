CREATE TABLE hero (
  version   INT,
  id        BIGINT NOT NULL PRIMARY KEY,
  name      VARCHAR(255),
  real_name VARCHAR(255),
  health    INT,
  armour    INT,
  shield    INT
);

CREATE TABLE ability (
  version     INT,
  id          BIGINT NOT NULL PRIMARY KEY,
  name        VARCHAR(255),
  description VARCHAR(2000),
  is_ultimate BOOLEAN
);

CREATE TABLE hero_ability (
  version    INT,
  hero_id    BIGINT NOT NULL REFERENCES hero (id),
  ability_id BIGINT NOT NULL REFERENCES ability (id)
);
