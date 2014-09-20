/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : meidi

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2014-09-17 10:58:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for inventory
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(4) DEFAULT NULL,
  `intime` date DEFAULT NULL,
  `chekid` int(11) DEFAULT NULL,
  `inbranchid` int(4) DEFAULT NULL,
  `outbranchid` int(4) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `outstatues` int(2) DEFAULT '0',
  `instatues` int(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for inventorymessage
-- ----------------------------
DROP TABLE IF EXISTS `inventorymessage`;
CREATE TABLE `inventorymessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` varchar(100) DEFAULT NULL,
  `categoryId` int(4) DEFAULT NULL,
  `count` int(4) DEFAULT NULL,
  `inventoryId` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdbranch
-- ----------------------------
DROP TABLE IF EXISTS `mdbranch`;
CREATE TABLE `mdbranch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(30) CHARACTER SET utf8 NOT NULL,
  `pid` int(11) NOT NULL,
  `bmessage` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=253 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mdbranchtype
-- ----------------------------
DROP TABLE IF EXISTS `mdbranchtype`;
CREATE TABLE `mdbranchtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(20) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mdcategory
-- ----------------------------
DROP TABLE IF EXISTS `mdcategory`;
CREATE TABLE `mdcategory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(20) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  `time` varchar(20) NOT NULL,
  `cstatues` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdcompany
-- ----------------------------
DROP TABLE IF EXISTS `mdcompany`;
CREATE TABLE `mdcompany` (
  `cname` varchar(100) NOT NULL,
  `uname` varchar(10) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `locate` varchar(50) NOT NULL,
  `locatedetail` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdgroup
-- ----------------------------
DROP TABLE IF EXISTS `mdgroup`;
CREATE TABLE `mdgroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(20) NOT NULL,
  `detail` varchar(50) NOT NULL,
  `statues` int(11) NOT NULL,
  `permissions` varchar(100) NOT NULL,
  `products` varchar(100) DEFAULT NULL,
  `glevel` int(2) DEFAULT NULL,
  `ptype` int(10) NOT NULL,
  `pid` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdgrouptype
-- ----------------------------
DROP TABLE IF EXISTS `mdgrouptype`;
CREATE TABLE `mdgrouptype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gname` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdinventorybranch
-- ----------------------------
DROP TABLE IF EXISTS `mdinventorybranch`;
CREATE TABLE `mdinventorybranch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inventoryid` int(4) DEFAULT NULL,
  `type` varchar(100) NOT NULL,
  `realcount` int(10) NOT NULL DEFAULT '0',
  `branchid` int(4) NOT NULL,
  `papercount` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdinventorybranchmessage
-- ----------------------------
DROP TABLE IF EXISTS `mdinventorybranchmessage`;
CREATE TABLE `mdinventorybranchmessage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branchid` int(4) DEFAULT NULL,
  `inventoryid` int(4) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `count` int(10) DEFAULT NULL,
  `operatortype` int(4) DEFAULT NULL,
  `realcount` int(10) DEFAULT '0',
  `papercount` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdlocate
-- ----------------------------
DROP TABLE IF EXISTS `mdlocate`;
CREATE TABLE `mdlocate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lname` varchar(50) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=47 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for mdmessage
-- ----------------------------
DROP TABLE IF EXISTS `mdmessage`;
CREATE TABLE `mdmessage` (
  `oid` int(11) NOT NULL,
  `mdmessage` text
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdorder
-- ----------------------------
DROP TABLE IF EXISTS `mdorder`;
CREATE TABLE `mdorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `andate` date DEFAULT NULL,
  `saledate` date NOT NULL,
  `pos` varchar(30) NOT NULL,
  `username` varchar(15) NOT NULL,
  `locates` varchar(20) NOT NULL,
  `locateDetail` varchar(100) NOT NULL,
  `saleID` int(11) NOT NULL,
  `printSatues` int(2) NOT NULL,
  `oderStatus` int(11) NOT NULL,
  `statuesinstall` int(2) DEFAULT '0',
  `sailId` varchar(30) NOT NULL,
  `checked` varchar(30) NOT NULL,
  `phone1` varchar(15) DEFAULT NULL,
  `printSatuesp` varchar(2) DEFAULT '0',
  `phone2` varchar(15) DEFAULT NULL,
  `remark` text NOT NULL,
  `sendId` int(4) DEFAULT '0',
  `deliveryStatues` int(4) NOT NULL,
  `orderbranch` varchar(20) NOT NULL,
  `categoryID` int(11) DEFAULT NULL,
  `statues1` int(2) DEFAULT '0',
  `statues2` int(2) DEFAULT '0',
  `statues3` int(2) DEFAULT '0',
  `dealSendid` int(4) DEFAULT '0',
  `statues4` int(2) DEFAULT '0',
  `statuesdingma` int(2) unsigned zerofill DEFAULT '00',
  `installid` int(4) DEFAULT '0',
  `submittime` datetime DEFAULT NULL,
  `printlnid` varchar(15) DEFAULT '',
  `statuescallback` int(2) DEFAULT '0',
  `statuespaigong` int(2) DEFAULT '0',
  `dayremark` int(10) DEFAULT '1',
  `dayID` int(5) DEFAULT '0',
  `sailIdremark` int(2) DEFAULT '0',
  `checkedremark` int(2) DEFAULT '0',
  `phoneRemark` int(2) DEFAULT '0',
  `posRemark` int(4) DEFAULT '0',
  `installTime` datetime DEFAULT NULL,
  `sendTime` datetime DEFAULT NULL,
  `deliverytype` int(2) DEFAULT '0',
  `returnid` int(4) DEFAULT '0',
  `returnprintstatues` int(1) DEFAULT '0',
  `returnstatues` int(1) DEFAULT '0',
  `returntime` datetime DEFAULT NULL,
  `returnwenyuan` int(1) DEFAULT '0',
  `printdingma` int(2) DEFAULT NULL,
  `dealsendTime` date DEFAULT NULL,
  `wenyuancallback` int(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=424 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdordergift
-- ----------------------------
DROP TABLE IF EXISTS `mdordergift`;
CREATE TABLE `mdordergift` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderid` int(11) NOT NULL,
  `giftName` varchar(20) NOT NULL,
  `statues` int(2) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=262 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdorderproduct
-- ----------------------------
DROP TABLE IF EXISTS `mdorderproduct`;
CREATE TABLE `mdorderproduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryID` int(15) DEFAULT NULL,
  `sendtype` varchar(100) DEFAULT NULL,
  `count` int(2) NOT NULL,
  `orderid` int(11) NOT NULL,
  `saletype` varchar(100) DEFAULT NULL,
  `statues` int(2) NOT NULL,
  `categoryname` varchar(50) NOT NULL,
  `salestatues` int(2) DEFAULT '-1',
  `subtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orderid` (`orderid`)
) ENGINE=MyISAM AUTO_INCREMENT=1348 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdorderupdateprint
-- ----------------------------
DROP TABLE IF EXISTS `mdorderupdateprint`;
CREATE TABLE `mdorderupdateprint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` text,
  `statues` int(11) NOT NULL,
  `orderid` int(11) NOT NULL,
  `mdtype` int(11) NOT NULL,
  `pGroupId` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orderid` (`orderid`)
) ENGINE=MyISAM AUTO_INCREMENT=978 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mdproduct
-- ----------------------------
DROP TABLE IF EXISTS `mdproduct`;
CREATE TABLE `mdproduct` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `ptype` varchar(50) NOT NULL,
  `categoryID` int(11) NOT NULL,
  `pstatues` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=383 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mduser
-- ----------------------------
DROP TABLE IF EXISTS `mduser`;
CREATE TABLE `mduser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `nickusername` varchar(20) DEFAULT NULL,
  `userpassword` varchar(20) NOT NULL,
  `entryTime` date DEFAULT NULL,
  `branch` varchar(30) NOT NULL,
  `positions` varchar(30) DEFAULT NULL,
  `usertype` int(11) NOT NULL,
  `products` varchar(50) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `charge` varchar(10) DEFAULT NULL,
  `statues` int(2) NOT NULL,
  `chargeid` int(5) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=398 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for verifycode
-- ----------------------------
DROP TABLE IF EXISTS `verifycode`;
CREATE TABLE `verifycode` (
  `saleorderno` varchar(20) NOT NULL,
  `verifycode` varchar(5) DEFAULT NULL,
  `detail` varchar(200) DEFAULT NULL,
  `recordtime` date DEFAULT NULL,
  PRIMARY KEY (`saleorderno`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of verifycode
-- ----------------------------

INSERT INTO `verifycode` VALUES ('00013040763701', '8560', '暂无', NOW());
INSERT INTO `verifycode` VALUES ('00013033467201', '6491', '暂无', NOW());
INSERT INTO `verifycode` VALUES ('00013274596201', '3472', '暂无', NOW());

