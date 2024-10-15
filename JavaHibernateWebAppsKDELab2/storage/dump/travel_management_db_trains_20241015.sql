CREATE DATABASE  IF NOT EXISTS `travel_management_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `travel_management_db`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: travel_management_db
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `trains`
--

DROP TABLE IF EXISTS `trains`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trains` (
  `departure_time` time(6) NOT NULL,
  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
  `number` varchar(6) NOT NULL,
  `duration_in_seconds` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `arrival_station` varchar(100) NOT NULL,
  `departure_station` varchar(100) NOT NULL,
  `movement_type` enum('DAILY','EVEN_DAYS','ODD_DAYS') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfl8n21arxdvb1hh98o4mn9csm` (`number`),
  KEY `idx_train_departure_station` (`departure_station`),
  KEY `idx_train_arrival_station` (`arrival_station`),
  KEY `idx_train_departure_time` (`departure_time`),
  CONSTRAINT `trains_chk_1` CHECK ((`departure_time` between '00:00:00.000000' and '23:59:59.000000')),
  CONSTRAINT `trains_chk_2` CHECK (regexp_like(`number`,_utf8mb4'^((?:00[1-9]|0[1-9][0-9]|1[0-4][0-9]|150|1(?:5[1-9]|[6-9][0-9])|2[0-8][0-9]|29[0-8]|30[1-9]|3[1-9][0-9]|4[0-4][0-9]|450|4(?:5[1-9]|[6-9][0-9])|5[0-8][0-9]|59[0-8]|60[1-9]|6[1-9][0-9]|70[1-9]|7[1-4][0-9]|750|75[1-9]|7[6-8][0-8])(?:ІС[+]?|РЕ|Р|НЕ|НШ|НП))$',_utf8mb4'c')),
  CONSTRAINT `trains_chk_3` CHECK ((`duration_in_seconds` between 0 and 86400)),
  CONSTRAINT `trains_chk_4` CHECK (regexp_like(`arrival_station`,_utf8mb4'^(([А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+|[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ[\']]+[.]?)(?:[ -](?:[А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+[0-9]*|[А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ[\']]+[.|0-9]*|[0-9]+)|[(][А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ[\']]+[.]?[)])*){1,100}$',_utf8mb4'c')),
  CONSTRAINT `trains_chk_5` CHECK (regexp_like(`departure_station`,_utf8mb4'^(([А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+|[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ[\']]+[.]?)(?:[ -](?:[А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+[0-9]*|[А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ[\']]+[.|0-9]*|[0-9]+)|[(][А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ[\']]+[.]?[)])*){1,100}$',_utf8mb4'c'))
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trains`
--

LOCK TABLES `trains` WRITE;
/*!40000 ALTER TABLE `trains` DISABLE KEYS */;
INSERT INTO `trains` VALUES ('14:30:00.000000',_binary '\0','102ІС',37800,1,'Одеса-Головна','Дніпро-Головний','EVEN_DAYS'),('10:15:00.000000',_binary '\0','203ІС',10500,2,'Львів','Івано-Франківськ','ODD_DAYS'),('06:45:00.000000',_binary '\0','101ІС+',21420,3,'Київ-Пасажирський','Львів','DAILY'),('06:05:00.000000',_binary '\0','345НШ',25800,4,'Дніпро-Головний','Миколаїв','EVEN_DAYS'),('22:10:00.000000',_binary '\0','304Р',29400,5,'Харків-Пасажирський','Кривий Ріг-Головний','DAILY'),('09:15:00.000000',_binary '\0','112НЕ',46800,6,'Полтава-Київська','Чернівці','EVEN_DAYS'),('03:20:00.000000',_binary '\0','405Р',40800,7,'Запоріжжя-1','Одеса-Головна','EVEN_DAYS'),('18:55:00.000000',_binary '\0','506Р',14400,8,'Київ-Пасажирський','Вінниця','ODD_DAYS'),('08:40:00.000000',_binary '\0','607РЕ',27000,9,'Миколаїв','Дніпро-Головний','DAILY'),('07:00:00.000000',_binary '\0','709РЕ',55800,10,'Чернівці','Одеса-Головна','EVEN_DAYS'),('19:45:00.000000',_binary '\0','710НЕ',25200,11,'Хмельницький','Київ-Пасажирський','ODD_DAYS'),('05:55:00.000000',_binary '\0','102НЕ',33000,12,'Івано-Франківськ','Житомир','DAILY'),('13:35:00.000000',_binary '\0','123НЕ',32400,13,'Одеса-Головна','Тернопіль','ODD_DAYS'),('16:50:00.000000',_binary '\0','234НШ',18600,14,'Кривий Ріг-Головний','Запоріжжя-1','DAILY'),('21:30:00.000000',_binary '\0','456НШ',21600,15,'Львів','Київ-Пасажирський','DAILY'),('11:25:00.000000',_binary '\0','708РЕ',7200,16,'Тернопіль','Львів','DAILY'),('11:55:00.000000',_binary '\0','567НП',50400,17,'Житомир','Львів','ODD_DAYS'),('07:35:00.000000',_binary '\0','556ІС+',39600,18,'Одеса-Головна','Запоріжжя-1','DAILY'),('15:25:00.000000',_binary '\0','678НП',14400,19,'Тернопіль','Хмельницький','EVEN_DAYS'),('23:10:00.000000',_binary '\0','689НП',36000,20,'Київ-Пасажирський','Одеса-Головна','DAILY'),('18:20:00.000000',_binary '\0','502НП',45000,21,'Львів','Одеса-Головна','ODD_DAYS'),('12:00:00.000000',_binary '\0','112ІС',10800,22,'Харків-Пасажирський','Полтава-Київська','EVEN_DAYS'),('03:45:00.000000',_binary '\0','223ІС',37800,23,'Миколаїв','Київ-Пасажирський','DAILY'),('14:55:00.000000',_binary '\0','334ІС',46800,24,'Тернопіль','Дніпро-Головний','EVEN_DAYS'),('09:05:00.000000',_binary '\0','590НП',72000,25,'Івано-Франківськ','Запоріжжя-1','DAILY'),('22:50:00.000000',_binary '\0','445ІС+',12600,26,'Чернівці','Львів','ODD_DAYS'),('17:40:00.000000',_binary '\0','667ІС+',64800,27,'Кривий Ріг-Головний','Івано-Франківськ','EVEN_DAYS'),('10:30:00.000000',_binary '\0','578ІС',28800,28,'Дніпро-Головний','Миколаїв','DAILY'),('19:15:00.000000',_binary '\0','589Р',25200,29,'Хмельницький','Київ-Пасажирський','ODD_DAYS'),('04:55:00.000000',_binary '\0','590РЕ',75600,30,'Запоріжжя-1','Львів','EVEN_DAYS');
/*!40000 ALTER TABLE `trains` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-15 20:15:04
