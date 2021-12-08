-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: web_demo_shiro
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `path` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `component` varchar(45) DEFAULT NULL,
  `redirect` varchar(45) DEFAULT NULL,
  `hidden` smallint DEFAULT NULL,
  `always_show` smallint DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'/maintainSysPermission','maintainSysPermission','layout',NULL,0,1,0,'2021-12-06 17:11:55','2021-12-06 17:11:55'),(2,'maintainUser','maintainUser','permission_user',NULL,0,0,1,'2021-12-06 17:12:21','2021-12-06 17:12:21'),(3,'maintainRole','maintainRole','permission_role',NULL,0,0,1,'2021-12-06 17:12:38','2021-12-06 17:12:38');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_meta`
--

DROP TABLE IF EXISTS `menu_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_meta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` int DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `no_cache` smallint DEFAULT NULL,
  `affix` smallint DEFAULT NULL,
  `breadcrumb` smallint DEFAULT NULL,
  `active_menu` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_meta`
--

LOCK TABLES `menu_meta` WRITE;
/*!40000 ALTER TABLE `menu_meta` DISABLE KEYS */;
INSERT INTO `menu_meta` VALUES (1,1,'管理系统权限','lock',0,0,0,NULL,'2021-12-06 17:13:51','2021-12-06 17:13:51'),(2,2,'维护用户',NULL,0,0,0,NULL,'2021-12-06 17:14:05','2021-12-06 17:14:05'),(3,3,'维护角色',NULL,0,0,0,NULL,'2021-12-06 17:14:12','2021-12-06 17:14:12');
/*!40000 ALTER TABLE `menu_meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation`
--

DROP TABLE IF EXISTS `operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operation_name` varchar(45) DEFAULT NULL,
  `operation_description` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation`
--

LOCK TABLES `operation` WRITE;
/*!40000 ALTER TABLE `operation` DISABLE KEYS */;
INSERT INTO `operation` VALUES (1,'create','增加操作','2021-12-06 17:16:49','2021-12-06 17:16:49'),(2,'update','更新操作','2021-12-06 17:17:03','2021-12-06 17:17:03'),(3,'delete','删除操作','2021-12-06 17:17:12','2021-12-06 17:17:12'),(4,'assignUserRole','分配用户角色操作','2021-12-06 17:17:28','2021-12-06 17:17:28'),(5,'assignRoleMenu','分配角色资源操作','2021-12-06 17:17:36','2021-12-06 17:17:36'),(6,'assignRolePermission','分配角色操作操作','2021-12-06 17:17:46','2021-12-06 17:17:46');
/*!40000 ALTER TABLE `operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(45) DEFAULT NULL,
  `permission_description` varchar(45) DEFAULT NULL,
  `resource_id` int DEFAULT NULL,
  `operation_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'createUser','创建用户',1,1,'2021-12-06 17:19:05','2021-12-06 17:19:05'),(2,'updateUser','更新用户',1,2,'2021-12-06 17:19:12','2021-12-06 17:19:12'),(3,'deleteUser','删除用户',1,3,'2021-12-06 17:19:19','2021-12-06 17:19:19'),(4,'assignUserRole','分配用户角色',1,4,'2021-12-06 17:19:57','2021-12-06 17:19:57'),(5,'createRole','创建角色',2,1,'2021-12-06 17:20:07','2021-12-06 17:20:07'),(6,'updateRole','更新角色',2,2,'2021-12-06 17:20:13','2021-12-06 17:20:13'),(7,'deleteRole','删除角色',2,3,'2021-12-06 17:20:18','2021-12-06 17:20:18'),(8,'assignRoleMenu','分配角色资源',2,5,'2021-12-06 17:20:27','2021-12-06 17:20:27'),(9,'assignRolePermission','分配角色操作',2,6,'2021-12-06 17:20:32','2021-12-06 17:20:32');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource` (
  `id` int NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(45) DEFAULT NULL,
  `resource_description` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'user','用户资源','2021-12-06 17:15:55','2021-12-06 17:15:55'),(2,'role','角色资源','2021-12-06 17:16:02','2021-12-06 17:16:02');
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) DEFAULT NULL,
  `role_description` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin','超级管理员','2021-12-06 17:09:31','2021-12-06 17:09:31'),(2,'editor','用户权限管理员','2021-12-06 17:10:02','2021-12-06 17:10:02'),(3,'common','普通用户','2021-12-06 17:10:14','2021-12-06 17:10:14');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `menu_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES (1,1,1,'2021-12-06 17:14:41','2021-12-06 17:14:41'),(2,1,2,'2021-12-06 17:14:44','2021-12-06 17:14:44'),(3,1,3,'2021-12-06 17:14:46','2021-12-06 17:14:46'),(4,2,1,'2021-12-06 17:14:49','2021-12-06 17:14:49'),(5,2,2,'2021-12-06 17:14:50','2021-12-06 17:14:50'),(6,2,3,'2021-12-06 17:14:52','2021-12-06 17:14:52');
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL,
  `permission_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1,1,'2021-12-06 17:21:03','2021-12-06 17:21:03'),(2,1,2,'2021-12-06 17:21:06','2021-12-06 17:21:06'),(3,1,3,'2021-12-06 17:21:07','2021-12-06 17:21:07'),(4,1,4,'2021-12-06 17:21:09','2021-12-06 17:21:09'),(5,1,5,'2021-12-06 17:21:10','2021-12-06 17:21:10'),(6,1,6,'2021-12-06 17:21:12','2021-12-06 17:21:12'),(7,1,7,'2021-12-06 17:21:14','2021-12-06 17:21:14'),(8,1,8,'2021-12-06 17:21:15','2021-12-06 17:21:15'),(9,1,9,'2021-12-06 17:21:17','2021-12-06 17:21:17'),(10,2,4,'2021-12-06 17:21:36','2021-12-06 17:21:36'),(11,2,5,'2021-12-06 17:21:37','2021-12-06 17:21:37'),(12,2,6,'2021-12-06 17:21:39','2021-12-06 17:21:39'),(13,2,7,'2021-12-06 17:21:41','2021-12-06 17:21:41'),(14,2,8,'2021-12-06 17:21:42','2021-12-06 17:21:42'),(15,2,9,'2021-12-06 17:21:44','2021-12-06 17:21:44');
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `salt` varchar(45) DEFAULT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `introduction` varchar(45) DEFAULT NULL,
  `avatar` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Cxx','1688c975994314f11de1afdda83a74b1','Vnvl3(TS','chenxiuxiang','超级管理员','profile.gif','2021-12-06 17:07:58','2021-12-06 17:07:58'),(2,'Cxx2','9d3aeff9ed350b6e641252982e2d97bc','tUDH)nnN','chenxiuxiang2','权限管理员','profile.gif','2021-12-06 17:08:23','2021-12-08 15:35:41'),(3,'Cxx3','a3960da00569c250d7af51d6581b61fd','gYaMu9us','chenxiuxiang3','普通用户','profile.gif','2021-12-08 15:34:38','2021-12-08 15:36:05');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1,1,'2021-12-06 17:10:48','2021-12-06 17:10:48'),(2,2,2,'2021-12-06 17:10:52','2021-12-06 17:10:52'),(3,3,3,'2021-12-06 17:10:56','2021-12-06 17:10:56');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-08 16:26:30
