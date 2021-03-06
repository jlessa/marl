DROP DATABASE if exists liquorstore;

CREATE DATABASE liquorstore;

USE liquorstore;

-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: liquorstore
-- ------------------------------------------------------
-- Server version	5.6.23-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `entry`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entry` (
  `entryId` int(11) NOT NULL AUTO_INCREMENT,
  `insertDate` datetime DEFAULT NULL,
  `processDate` datetime DEFAULT NULL,
  PRIMARY KEY (`entryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entry`
--

LOCK TABLES `entry` WRITE;
/*!40000 ALTER TABLE `entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itemstoreorder`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `itemstoreorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` bigint(20) DEFAULT NULL,
  `productInternalId` int(11) DEFAULT NULL,
  `productSupplierId` int(11) DEFAULT NULL,
  `STOREORDERJPA_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `I_TMSTRDR_PRODUCTJPA` (`productInternalId`,`productSupplierId`),
  KEY `I_TMSTRDR_STOREORDERJPA` (`STOREORDERJPA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itemstoreorder`
--

LOCK TABLES `itemstoreorder` WRITE;
/*!40000 ALTER TABLE `itemstoreorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `itemstoreorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itemsupplierorder`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `itemsupplierorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` bigint(20) DEFAULT NULL,
  `SUPPLIERORDERJPA_ID` int(11) DEFAULT NULL,
  `productInternalId` int(11) DEFAULT NULL,
  `productSupplierId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `I_TMSPRDR_PRODUCTJPA` (`productInternalId`,`productSupplierId`),
  KEY `I_TMSPRDR_SUPPLIERORDERJPA` (`SUPPLIERORDERJPA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itemsupplierorder`
--

LOCK TABLES `itemsupplierorder` WRITE;
/*!40000 ALTER TABLE `itemsupplierorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `itemsupplierorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openjpa_sequence_table`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openjpa_sequence_table` (
  `ID` tinyint(4) NOT NULL,
  `SEQUENCE_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openjpa_sequence_table`
--

LOCK TABLES `openjpa_sequence_table` WRITE;
/*!40000 ALTER TABLE `openjpa_sequence_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `openjpa_sequence_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `internalId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `productSupplierId` int(11) NOT NULL,
  `SUPPLIERJPA_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`internalId`),
  UNIQUE KEY `U_PRODUCT_PRODUCTSUPPLIERID` (`productSupplierId`),
  KEY `I_PRODUCT_SUPPLIERJPA` (`SUPPLIERJPA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Heineken',1,1),(2,'Stella Artois',2,1),(3,'Paulaner',3,2),(4,'Therezopolis',4,3),(5,'St. Gallen',5,3),(6,'Leffe',6,1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,'Store A'),(2,'Store B'),(3,'Store C');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storeorder`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storeorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `insertDate` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `entryId` int(11) NOT NULL,
  `STOREJPA_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `I_STRORDR_ENTRY` (`entryId`),
  KEY `I_STRORDR_STOREJPA` (`STOREJPA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storeorder`
--

LOCK TABLES `storeorder` WRITE;
/*!40000 ALTER TABLE `storeorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `storeorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'Supplier A'),(2,'Supplier B'),(3,'Supplier C');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplierorder`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `supplierorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `insertDate` datetime DEFAULT NULL,
  `orderNumber` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `SUPPLIERJPA_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `I_SPPLRDR_SUPPLIERJPA` (`SUPPLIERJPA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplierorder`
--

LOCK TABLES `supplierorder` WRITE;
/*!40000 ALTER TABLE `supplierorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplierorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplierorder_storeorder`
--


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `supplierorder_storeorder` (
  `SUPPLIERORDERJPA_ID` int(11) DEFAULT NULL,
  `STOREORDERLIST_ID` int(11) DEFAULT NULL,
  KEY `I_SPPLRDR_ELEMENT` (`STOREORDERLIST_ID`),
  KEY `I_SPPLRDR_SUPPLIERORDERJPA_ID` (`SUPPLIERORDERJPA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplierorder_storeorder`
--

LOCK TABLES `supplierorder_storeorder` WRITE;
/*!40000 ALTER TABLE `supplierorder_storeorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplierorder_storeorder` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-06-09 16:56:46