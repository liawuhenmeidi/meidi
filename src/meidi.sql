/*
Navicat MySQL Data Transfer

Date: 2014-07-12 14:03:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mdbranch
-- ----------------------------
DROP TABLE IF EXISTS `mdbranch`;
CREATE TABLE `mdbranch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(20) CHARACTER SET utf8 NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=107 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of mdbranch
-- ----------------------------


-- ----------------------------
-- Table structure for mdbranchtype
-- ----------------------------
DROP TABLE IF EXISTS `mdbranchtype`;
CREATE TABLE `mdbranchtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(20) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of mdbranchtype
-- ----------------------------

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
) ENGINE=MyISAM AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdcategory
-- ----------------------------

-- ----------------------------
-- Table structure for mdgroup
-- ----------------------------
DROP TABLE IF EXISTS `mdgroup`;
CREATE TABLE `mdgroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(20) NOT NULL,
  `detail` varchar(50) NOT NULL,
  `statues` int(11) NOT NULL,
  `permissions` varchar(30) NOT NULL,
  `products` varchar(30) DEFAULT NULL,
  `glevel` int(2) DEFAULT NULL,
  `pid` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdgroup
-- ----------------------------

INSERT INTO `mdgroup` VALUES ('1', '管理员组', '系统最高权限组', '1', '0_1_4_3_2_5_6_7_8_9_10_11_', '11_10_9_8_33_38_39_40_41', null, '1');


-- ----------------------------
-- Table structure for mdlocate
-- ----------------------------
DROP TABLE IF EXISTS `mdlocate`;
CREATE TABLE `mdlocate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lname` varchar(50) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of mdlocate
-- ----------------------------

-- ----------------------------
-- Table structure for mdorder
-- ----------------------------
DROP TABLE IF EXISTS `mdorder`;
CREATE TABLE `mdorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `andate` date NOT NULL,
  `saledate` date NOT NULL,
  `pos` varchar(30) NOT NULL,
  `username` varchar(15) NOT NULL,
  `locates` varchar(20) NOT NULL,
  `locateDetail` varchar(20) NOT NULL,
  `saleID` int(11) NOT NULL,
  `printSatues` int(11) NOT NULL,
  `oderStatus` int(11) NOT NULL,
  `sailId` varchar(20) NOT NULL,
  `checked` varchar(20) NOT NULL,
  `phone1` varchar(15) NOT NULL,
  `phone2` varchar(15) DEFAULT NULL,
  `remark` text NOT NULL,
  `sendId` int(4) DEFAULT NULL,
  `deliveryStatues` int(4) NOT NULL,
  `orderbranch` varchar(20) NOT NULL,
  `categoryID` int(11) DEFAULT NULL,
  `statues1` int(2) DEFAULT '0',
  `statues2` int(2) DEFAULT '0',
  `statues3` int(2) DEFAULT '0',
  `dealSendid` int(4) DEFAULT '0',
  `statues4` int(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdorder
-- ----------------------------

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
) ENGINE=MyISAM AUTO_INCREMENT=158 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdordergift
-- ----------------------------

-- ----------------------------
-- Table structure for mdorderproduct
-- ----------------------------
DROP TABLE IF EXISTS `mdorderproduct`;
CREATE TABLE `mdorderproduct` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryID` int(15) DEFAULT NULL,
  `sendtype` varchar(20) DEFAULT NULL,
  `count` int(2) NOT NULL,
  `orderid` int(11) NOT NULL,
  `saletype` varchar(20) DEFAULT NULL,
  `statues` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `orderid` (`orderid`)
) ENGINE=MyISAM AUTO_INCREMENT=476 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdorderproduct
-- ----------------------------

-- ----------------------------
-- Table structure for mdorderupdateprint
-- ----------------------------
DROP TABLE IF EXISTS `mdorderupdateprint`;
CREATE TABLE `mdorderupdateprint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` text NOT NULL,
  `statues` int(11) NOT NULL,
  `orderid` int(11) NOT NULL,
  `mdtype` int(11) NOT NULL,
  `pGroupId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orderid` (`orderid`)
) ENGINE=MyISAM AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdorderupdateprint
-- ----------------------------

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
) ENGINE=MyISAM AUTO_INCREMENT=274 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mdproduct
-- ----------------------------


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
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mduser 
-- ----------------------------
INSERT INTO `mduser` VALUES ('1', '管理员', null, '123456', NOW(), '', '经理', '1', null, '', null, '1');
 
-- ----------------------------
-- Table structure for xlactivity
-- ----------------------------
DROP TABLE IF EXISTS `xlactivity`;
CREATE TABLE `xlactivity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `xlusername` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `conpeny` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xlactivity
-- ----------------------------

