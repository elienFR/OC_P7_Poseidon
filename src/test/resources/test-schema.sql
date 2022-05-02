-- Create database
create database if not exists poseidontest;

use poseidontest;

-- Dropping all tables first
drop table if exists bidlist;
drop table if exists curvepoint;
drop table if exists rating;
drop table if exists rulename;
drop table if exists trade;
drop table if exists authorities;
drop table if exists users;

-- poseidon.bidlist definition

CREATE TABLE `bidlist` (
  `BidListId` tinyint NOT NULL AUTO_INCREMENT,
  `account` varchar(30) NOT NULL,
  `type` varchar(30) NOT NULL,
  `bidQuantity` double DEFAULT NULL,
  `askQuantity` double DEFAULT NULL,
  `bid` double DEFAULT NULL,
  `ask` double DEFAULT NULL,
  `benchmark` varchar(125) DEFAULT NULL,
  `bidListDate` timestamp NULL DEFAULT NULL,
  `commentary` varchar(125) DEFAULT NULL,
  `security` varchar(125) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `trader` varchar(125) DEFAULT NULL,
  `book` varchar(125) DEFAULT NULL,
  `creationName` varchar(125) DEFAULT NULL,
  `creationDate` timestamp NULL DEFAULT NULL,
  `revisionName` varchar(125) DEFAULT NULL,
  `revisionDate` timestamp NULL DEFAULT NULL,
  `dealName` varchar(125) DEFAULT NULL,
  `dealType` varchar(125) DEFAULT NULL,
  `sourceListId` varchar(125) DEFAULT NULL,
  `side` varchar(125) DEFAULT NULL,
  PRIMARY KEY (`BidListId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- poseidon.curvepoint definition

CREATE TABLE `curvepoint` (
  `Id` tinyint NOT NULL AUTO_INCREMENT,
  `CurveId` tinyint DEFAULT NULL,
  `asOfDate` timestamp NULL DEFAULT NULL,
  `term` double DEFAULT NULL,
  `value` double DEFAULT NULL,
  `creationDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- poseidon.rating definition

CREATE TABLE `rating` (
  `Id` tinyint NOT NULL AUTO_INCREMENT,
  `moodysRating` varchar(125) DEFAULT NULL,
  `sandPRating` varchar(125) DEFAULT NULL,
  `fitchRating` varchar(125) DEFAULT NULL,
  `orderNumber` tinyint DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- poseidon.rulename definition

CREATE TABLE `rulename` (
  `Id` tinyint NOT NULL AUTO_INCREMENT,
  `name` varchar(125) DEFAULT NULL,
  `description` varchar(125) DEFAULT NULL,
  `json` varchar(125) DEFAULT NULL,
  `template` varchar(512) DEFAULT NULL,
  `sqlStr` varchar(125) DEFAULT NULL,
  `sqlPart` varchar(125) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- poseidon.trade definition

CREATE TABLE `trade` (
  `TradeId` tinyint NOT NULL AUTO_INCREMENT,
  `account` varchar(30) NOT NULL,
  `type` varchar(30) NOT NULL,
  `buyQuantity` double DEFAULT NULL,
  `sellQuantity` double DEFAULT NULL,
  `buyPrice` double DEFAULT NULL,
  `sellPrice` double DEFAULT NULL,
  `tradeDate` timestamp NULL DEFAULT NULL,
  `security` varchar(125) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `trader` varchar(125) DEFAULT NULL,
  `benchmark` varchar(125) DEFAULT NULL,
  `book` varchar(125) DEFAULT NULL,
  `creationName` varchar(125) DEFAULT NULL,
  `creationDate` timestamp NULL DEFAULT NULL,
  `revisionName` varchar(125) DEFAULT NULL,
  `revisionDate` timestamp NULL DEFAULT NULL,
  `dealName` varchar(125) DEFAULT NULL,
  `dealType` varchar(125) DEFAULT NULL,
  `sourceListId` varchar(125) DEFAULT NULL,
  `side` varchar(125) DEFAULT NULL,
  PRIMARY KEY (`TradeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- poseidon.users definition

CREATE TABLE `users` (
  `Id` tinyint NOT NULL AUTO_INCREMENT,
  `username` varchar(125) DEFAULT NULL,
  `password` varchar(125) DEFAULT NULL,
  `fullname` varchar(125) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `users_un` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- poseidon.authorities definition

CREATE TABLE `authorities` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `userid` tinyint NOT NULL,
  `role` varchar(125) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `authorities_FK` (`userid`),
  CONSTRAINT `authorities_FK` FOREIGN KEY (`userid`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
