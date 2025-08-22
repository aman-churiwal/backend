LOCK TABLES `${sqlTableName}` WRITE;
/*!40000 ALTER TABLE `${sqlTableName}` DISABLE KEYS */;
INSERT INTO `${sqlTableName}` (`name`) VALUES (...);
/*!40000 ALTER TABLE `${sqlTableName}` ENABLE KEYS */;
UNLOCK TABLES;