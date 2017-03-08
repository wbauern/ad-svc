DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`user_id` INT NOT NULL,
`username` varchar(100) NOT NULL UNIQUE,
PRIMARY KEY (`user_id`)
);

DROP TABLE IF EXISTS `user_size_assoc`;
CREATE TABLE `user_size_assoc` (
`user_id` INT NOT NULL,
`user_size_assoc_id` INT NOT NULL,
`ad_size_id` INT NOT NULL,
PRIMARY KEY (`user_size_assoc_id`)
);

DROP TABLE IF EXISTS `ad_size`;
CREATE TABLE `ad_size` (
`ad_size_id` INT NOT NULL,
`width` INT NOT NULL,
`height` INT NOT NULL,
PRIMARY KEY (`ad_size_id`)
);

DROP TABLE IF EXISTS `provider_size_assoc`;
CREATE TABLE `provider_size_assoc` (
`provider_size_assoc_id` INT NOT NULL,
`ad_size_id` INT NOT NULL,
`provider_id` INT NOT NULL,
PRIMARY KEY (`provider_size_assoc_id`)
);

DROP TABLE IF EXISTS `provider`;
CREATE TABLE `provider` (
`provider_id` INT NOT NULL,
`provider_name` varchar(64) NOT NULL UNIQUE,
`url` varchar(512) NOT NULL UNIQUE,
PRIMARY KEY (`provider_id`)
);

DROP TABLE IF EXISTS `user_provider_assoc`;
CREATE TABLE `user_provider_assoc` (
`user_provider_assoc_id` INT NOT NULL,
`user_id` INT NOT NULL,
`provider_id` INT NOT NULL,
PRIMARY KEY (`user_provider_assoc_id`)
);

ALTER TABLE `user_size_assoc` ADD CONSTRAINT `user_size_assoc_fk0` FOREIGN KEY (`user_id`)
REFERENCES `user`(`user_id`);
ALTER TABLE `user_size_assoc` ADD CONSTRAINT `user_size_assoc_fk1` FOREIGN KEY
(`ad_size_id`) REFERENCES `ad_size`(`ad_size_id`);
ALTER TABLE `provider_size_assoc` ADD CONSTRAINT `provider_size_assoc_fk0` FOREIGN KEY
(`ad_size_id`) REFERENCES `ad_size`(`ad_size_id`);
ALTER TABLE `provider_size_assoc` ADD CONSTRAINT `provider_size_assoc_fk1` FOREIGN KEY
(`provider_id`) REFERENCES `provider`(`provider_id`);
ALTER TABLE `user_provider_assoc` ADD CONSTRAINT `user_provider_assoc_fk0` FOREIGN KEY
(`user_id`) REFERENCES `user`(`user_id`);
