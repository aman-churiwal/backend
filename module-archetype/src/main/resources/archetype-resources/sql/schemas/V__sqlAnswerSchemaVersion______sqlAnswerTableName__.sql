CREATE DATABASE  IF NOT EXISTS `viteezy` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `viteezy`;

--
-- Table structure for table `${sqlAnswerTableName}`
--

DROP TABLE IF EXISTS `${sqlAnswerTableName}`;
CREATE TABLE `${sqlAnswerTableName}` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `quiz_id` int(11) NOT NULL,
                           `${moduleNameSnakeCase}_id` int(11) NOT NULL ,
                           `creation_timestamp` timestamp DEFAULT NOW(),
                           `modification_timestamp` timestamp DEFAULT NOW(),
                           PRIMARY KEY (`id`),
                           CONSTRAINT ${sqlAnswerTableName}_pk_2
                               UNIQUE (quiz_id, ${moduleNameSnakeCase}_id),
                           CONSTRAINT ${sqlAnswerTableName}_${sqlTableName}_id_fk
                               FOREIGN KEY (${moduleNameSnakeCase}_id) REFERENCES ${sqlTableName} (id),
                           CONSTRAINT ${sqlAnswerTableName}_quiz_id_fk
                               FOREIGN KEY (quiz_id) REFERENCES quiz (id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
