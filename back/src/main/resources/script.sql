-- Table SUBJECTS
CREATE TABLE `subjects` ( 
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(255),
  `description` VARCHAR(2000),
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table USERS
CREATE TABLE `users` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL UNIQUE,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table POSTS
CREATE TABLE `posts` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(255),
  `content` TEXT,
  `subject_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

-- Table COMMENTS
CREATE TABLE `comments` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `content` VARCHAR(2000),
  `user_id` INT NOT NULL,
  `post_id` INT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

-- Table SUBSCRIPTION (relation ManyToMany User <-> Subject)
CREATE TABLE `subscription` (
  `user_id` INT NOT NULL, 
  `subject_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `subject_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`)
);

-- Table REFRESH_TOKENS
CREATE TABLE `refresh_tokens` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL UNIQUE,
  `user_id` INT NOT NULL,
  `expiry_date` DATETIME NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

-- Données initiales SUBJECTS
INSERT INTO `subjects` (`name`, `description`)
VALUES 
  ('Cybersécurité', 'La cybersécurité est l\'ensemble des méthodes, processus, outils et comportements qui protègent les systèmes informatiques, les réseaux et les données contre les cyberattaques et les accès non autorisés qui pourraient causer du tord'),
  ('Gestion de projet', 'La gestion de projet est la mise en œuvre contrôlée du plan de projet sous la direction de la direction générale de l\'organisation . Traditionnellement, un projet réussi est celui qui a livré ses produits ou services conformément au plan de projet, répondant ainsi aux objectifs généraux de l\'entreprise.'),
  ('Infrastructure', 'L\'infrastructure informatique regroupe l\'ensemble des équipements matériels (postes de travail, serveurs, routeurs, périphériques…) et des logiciels (ERP, CRM, messagerie, réseau…) d\'une entreprise. Elle représente l\'agencement entre : les différentes applications, le service de stockage et le réseau d\'entreprise.'),
  ('Architecture informatique', 'L\'architecture informatique constitue la colonne vertébrale de tout système d\'information. Elle définit comment les composants logiciels et matériels interagissent pour accomplir des tâches spécifiques, et permet d\'assurer la scalabilité, la fiabilité, la sécurité et l\'efficacité des systèmes, sur la durée.'),
  ('Nouvelles technologies', 'Les NTIC regroupent les innovations réalisées en matière de volume de stockage et de rapidité du traitement de l\'information ainsi que son transport grâce au numérique et aux nouveaux moyens de télécommunication (fibre optique, câble, satellites, techniques sans fil).');
  ('Back-end', 'Le back end est l\'épine dorsale de toute application web, gérant les opérations en coulisse qui rendent possible l\'expérience utilisateur visible sur le front end. Il est essentiel pour le traitement des données, la sécurité, la performance et la scalabilité de l\'application')

-- Pour la démo : password = Azerty51!
INSERT INTO `users` (`username`, `email`, `password`) VALUES (
  'JohnDoe', 'johndoe@gmail.com', '$2a$10$Y1RnseCeaHenekQ385uHseI4ipSklMazNDp2JxFvhUoZT1w.JkjlK'
) 

INSERT INTO `posts` (`title`, `content`, `subject_id`, `user_id`) VALUES (
  'Lorem Ipsum Dabo',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna 
  aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
  Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
  occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."Lorem ipsum dolor sit
  amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
  veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in
  reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
  proident, sunt in culpa qui officia deserunt mollit anim id est laborum."Lorem ipsum dolor sit amet, consectetur
  adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis
  nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit
  in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt
  n culpa qui officia deserunt mollit anim id est laborum."Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed
  do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullam
  co laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse ci
  llum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deseru
  nt mollit anim id est laborum."',
  1,
  1
)