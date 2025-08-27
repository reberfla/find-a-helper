DROP DATABASE IF EXISTS findahelper;
CREATE DATABASE IF NOT EXISTS findahelper DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER IF NOT EXISTS 'dev'@'%' IDENTIFIED BY 'dev';
GRANT ALL PRIVILEGES ON findahelper.* TO 'dev'@'%';
FLUSH PRIVILEGES;

USE findahelper;

/*Table structure for table assignments */

CREATE TABLE assignments (
  id int(11) NOT NULL AUTO_INCREMENT,
  task_id int(11) NOT NULL,
  offer_id int(11) NOT NULL,
  status enum('IN_PROGRESS','OPEN','COMPLETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OPEN',
  active tinyint(1) NOT NULL,
  created_at datetime DEFAULT current_timestamp(),
  CONSTRAINT assignments_pk PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table offers */

CREATE TABLE offers (
  user_id int(11) NOT NULL,
  task_id int(11) NOT NULL,
  text text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  title varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  status enum('SUBMITTED','ACCEPTED','REJECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SUBMITTED',
  created_at datetime DEFAULT current_timestamp(),
  active tinyint(1) NOT NULL DEFAULT 1,
  valid_until date DEFAULT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  CONSTRAINT offers_pk PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table ratings */

CREATE TABLE ratings (
  assignment_id int(11) NOT NULL,
  stars int(11) NOT NULL CHECK (stars >= 1 and stars <= 5),
  message text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  created_at datetime NOT NULL DEFAULT current_timestamp(),
  id int(11) NOT NULL AUTO_INCREMENT,
  requester_id int(11) NOT NULL COMMENT 'Der Auftraggeber (stellt Auftrag ein)',
  offerer_id int(11) NOT NULL COMMENT 'Der Helfer (Anbieter der Angebot)',
  CONSTRAINT ratings_pk PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*Table structure for table tasks */

CREATE TABLE tasks (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  zip_code varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  coordinates text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  title varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  description text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  category enum('SHOPPING','TRANSPORT','CLEANING','PETCARE','GARDENING','TUTORING','TECHHELP','CHILDCARE','LANGUAGETANDEM','HOMEWORK','REPAIRS','OTHERS') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OTHERS',
  status enum('OPEN','ASSIGNED','COMPLETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OPEN',
  active tinyint(1) NOT NULL DEFAULT 1,
  deadline bigint DEFAULT NULL,
  task_interval enum('CONTINUOUS','RECURRING','ONE_TIME') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ONE_TIME',
  weekdays text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  created_at bigint DEFAULT NULL,
  CONSTRAINT task_pk PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


/*Table structure for table users */

CREATE TABLE users (
    name varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    email varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    password_hash varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    zip_code varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    image longblob DEFAULT NULL,
    image_url text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    active tinyint(1) NOT NULL DEFAULT 1,
    locked_until bigint DEFAULT NULL,
    last_token_issued bigint DEFAULT NULL,
    auth_provider enum('GOOGLE','LOCAL') NOT NULL DEFAULT 'LOCAL',
    id int(11) NOT NULL AUTO_INCREMENT,
    birthdate date NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    UNIQUE KEY email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/* Define References */

ALTER TABLE assignments
  ADD CONSTRAINT fk_assignments_offer FOREIGN KEY (offer_id) REFERENCES offers(id),
  ADD CONSTRAINT fk_assignments_task FOREIGN KEY (task_id) REFERENCES tasks(id);

ALTER TABLE offers
  ADD CONSTRAINT fk_offers_task FOREIGN KEY (task_id) REFERENCES tasks(id),
  ADD CONSTRAINT fk_offers_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ratings
  ADD CONSTRAINT fk_ratings_assigments FOREIGN KEY (assignment_id) REFERENCES assignments(id),
  ADD CONSTRAINT fk_requester_tasks FOREIGN KEY (requester_id) REFERENCES users(id),
  ADD CONSTRAINT fk_offerer_offers FOREIGN KEY (offerer_id) REFERENCES users(id);

ALTER TABLE tasks
  ADD CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id);
