CREATE TABLE hero (
  version   INT,
  id        BIGINT PRIMARY KEY,
  name      VARCHAR(255),
  real_name VARCHAR(255),
  health    INT,
  armour    INT,
  shield    INT
);

CREATE TABLE ability (
  version     INT,
  id          BIGINT PRIMARY KEY,
  name        VARCHAR(255),
  description VARCHAR(2000),
  is_ultimate BOOLEAN
);
