# ************************************************************
# Sequel Pro SQL dump
# Version 4135
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 5.5.42)
# Database: JMessanger
# Generation Time: 2015-09-11 06:41:43 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table chat
# ------------------------------------------------------------

CREATE TABLE  'chat' (
  'c_id' int(11) NOT NULL AUTO_INCREMENT,
  'u_primo' int(11) NOT NULL,
  'u_secondo' int(11) NOT NULL,
  PRIMARY KEY ('c_id'),
  FOREIGN KEY('u_primo') references 'users'('id'),
  FOREIGN KEY('u_secondo') references 'users'('id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table messaggi
# ------------------------------------------------------------

CREATE TABLE 'messaggi' (
  'm_id' int(11) NOT NULL AUTO_INCREMENT,
  'c_id' int(11) NOT NULL,
  'u_mitt' int(11) NOT NULL,
  'u_dest' int(11) NOT NULL,
  'messaggio' varchar(255) DEFAULT NULL,
  'inviato' tinyint(1) NOT NULL,
  PRIMARY KEY ('m_id'),
  FOREIGN KEY('c_id') references 'chat'('id'),
  FOREIGN KEY('u_mit') references 'users'('id'),
  FOREIGN KEY('u_dest') references 'users'('id')



) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES 'messaggi' WRITE;
/*!40000 ALTER TABLE 'messaggi' DISABLE KEYS */;

INSERT INTO 'messaggi' ('m_id', 'c_id', 'u_mitt', 'u_dest', 'messaggio', 'inviato')
VALUES
  (54,1,3,1,'Messaggio di prova',0);

/*!40000 ALTER TABLE 'messaggi' ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------


CREATE TABLE 'users' (
  'id' int(11) NOT NULL AUTO_INCREMENT,
  'nome' varchar(50) DEFAULT NULL,
  'numero' varchar(13) DEFAULT NULL,
  'ip' varchar(12) DEFAULT NULL,
  'macaddress' varchar(48) DEFAULT NULL,
  'attivo' tinyint(1) DEFAULT NULL,
  PRIMARY KEY ('id')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES 'users' WRITE;
/*!40000 ALTER TABLE 'users' DISABLE KEYS */;

INSERT INTO 'users' ('id', 'nome', 'numero', 'ip', 'macaddress', 'attivo')
VALUES
  (1,'Giovanni Dispoto','0003318669067','0.0.0.0','48-2C-6A-1E-59-3D',1),
  (3,'Giuseppe Dispoto','0003477831234','0.0.0.0','48-2C-6A-1E-59-3D',1);

/*!40000 ALTER TABLE 'users' ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
