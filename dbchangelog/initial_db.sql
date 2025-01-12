CREATE DATABASE  IF NOT EXISTS `nextgen_shop`;
-- DEFAULT CHARACTER SET utf8mb4
-- COLLATE utf8mb4_0900_ai_ci
-- DEFAULT ENCRYPTION='N'
USE `nextgen_shop`;

-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
-- Database: nextgen_shop
-- Server version	8.0.37


-- Table structure for table `account`
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
    `ID` int NOT NULL AUTO_INCREMENT,
    `username` varchar(45) NOT NULL,
    `password` varchar(255) NOT NULL,
    `email` varchar(100) NOT NULL,
    `phone` varchar(20) DEFAULT NULL,
    `address` varchar(512) DEFAULT NULL,
    `device_ID` varchar(45) DEFAULT NULL,
    `token` varchar(512) DEFAULT NULL,
    `created_by` varchar(45) NOT NULL,
    `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_by` varchar(45) DEFAULT NULL,
    `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `ID_UNIQUE` (`ID`),
    UNIQUE KEY `username_UNIQUE` (`username`),
    UNIQUE KEY `email_UNIQUE` (`email`),
    UNIQUE KEY `device_ID_UNIQUE` (`device_ID`),
    KEY `phone_INDEX` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `authority`

DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
    `ID` int NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `code` varchar(45) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `parent_ID` int DEFAULT NULL,
    `parent_code` varchar(45) DEFAULT NULL,
    `created_by` varchar(45) NOT NULL,
    `last_modified_by` varchar(45) DEFAULT NULL,
    `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `code_UNIQUE` (`code`),
    KEY `parent_ID_INDEX` (`parent_ID`),
    KEY `parent_code_INDEX` (`parent_code`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `role`

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `ID` int NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `code` varchar(45) NOT NULL,
    `created_by` varchar(45) NOT NULL,
    `last_modified_by` varchar(45) DEFAULT NULL,
    `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `role_permission`

DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
    `ID` int NOT NULL AUTO_INCREMENT,
    `role_ID` int NOT NULL,
    `role_code` varchar(45) NOT NULL,
    `permission_ID` int NOT NULL,
    `permission_code` varchar(45) NOT NULL,
    `permission_parent_code` varchar(45) DEFAULT NULL,
    `created_by` varchar(45) NOT NULL,
    `last_modified_by` varchar(45) DEFAULT NULL,
    `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`),
    KEY `role_ID_permission_ID_INDEX` (`role_ID`,`permission_ID`),
    KEY `role_code_permission_code_INDEX` (`role_code`,`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `user_role`

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
    `ID` int NOT NULL AUTO_INCREMENT,
    `user_ID` int NOT NULL,
    `role_ID` int NOT NULL,
    `created_by` varchar(45) NOT NULL,
    `last_modified_by` varchar(45) DEFAULT NULL,
    `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`),
    KEY `user_ID_role_ID_INDEX` (`user_ID`,`role_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `system_config`

DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
    `id` int NOT NULL AUTO_INCREMENT,
    `code` varchar(45) NOT NULL,
    `content` text,
    `description` varchar(255) DEFAULT NULL,
    `enabled` int NOT NULL,
    `created_by` varchar(45) NOT NULL,
    `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_by` varchar(45) DEFAULT NULL,
    `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
