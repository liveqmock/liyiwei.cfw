USE cfw;

DROP TABLE IF EXISTS channel;
CREATE TABLE channel (
  id              Int          NOT NULL auto_increment,
  name            Varchar(80)  NOT NULL,
  seq             Int,
  parentId        Int,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS article;
CREATE TABLE article (
  id              Int          NOT NULL auto_increment,
  channelId       Int          NOT NULL,
  title           Varchar(120) NOT NULL,
  subTitle        Varchar(120),
  brief	          Varchar(1000),
  smallPic	      Varchar(80),
  content         MediumText,
  author          Varchar(40),
  tag             Varchar(60),
  source	      Varchar(60),
  status	      Int(4)       NOT NULL,
  viewCount	      Int          NOT NULL default 0,
  approveStatus	  Int(4)       NOT NULL default 0,
  approveUserId	  Int,
  approveTime	  Datetime,
  approveNote	  Varchar(400),
  createUserId    Int          NOT NULL,
  createTime      Datetime     NOT NULL,
  modifyUserId    Int          NOT NULL,
  modifyTime      Datetime     NOT NULL,
  PRIMARY KEY  (id), 
  KEY INDEX_CHANNEL_ID (channelId),
  KEY INDEX_TITLE (title),
  KEY INDEX_MODIFY_USER_ID (modifyUserId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS area;
CREATE TABLE area (
  id              Int          NOT NULL auto_increment,
  name	          Varchar(100) NOT NULL,
  code	          Varchar(12)  NOT NULL,
  type	          Int(4)       NOT NULL,
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS category;
CREATE TABLE category (
  id              Int          NOT NULL auto_increment,
  name	          Varchar(40)  NOT NULL,
  seq             Int,
  parentId	      Int,
  PRIMARY KEY  (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS department;
CREATE TABLE department (
  id              Int          NOT NULL auto_increment,
  name            Varchar(60)  NOT NULL,
  code            Varchar(20),
  note            Varchar(500),
  seq             Int          default 10,
  parentId        Int,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS sysUser;
CREATE TABLE sysUser (
  id              Int          NOT NULL auto_increment,
  username        Varchar(20)  NOT NULL,
  password        Varchar(255) NOT NULL,
  departmentId    Int, 
  realname        Varchar(40),
  title           Varchar(40),
  email           Varchar(60),
  mobile          Varchar(60),
  ext             Varchar(20),
  note            Varchar(500),
  rawPassword     Varchar(12),
  rank	          Int(4),
  roles           Varchar(100),
  status          Int(4)       NOT NULL,
  salt	          Varchar(255),
  screenHeight	  Int,
  screenWidth	  Int,
  rowNum	      Int,
  registerTime    Datetime     NOT NULL,
  lastVisitTime   Datetime,
  lastIp          Varchar(20),
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS role;
CREATE TABLE role (
  id              Int          NOT NULL auto_increment,
  name            Varchar(40)  NOT NULL,
  note            Varchar(500),
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS privilege;
CREATE TABLE privilege (
  id              Int          NOT NULL auto_increment,
  name            Varchar(40)  NOT NULL,
  type	          Int(4),
  objectId	      Int,
  note            Varchar(100),
  parentId        Int,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS rolePrivilege;
CREATE TABLE rolePrivilege (
  id              Int          NOT NULL auto_increment,
  roleId          Int          NOT NULL,
  privilegeId     Int          NOT NULL,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS attachment;
CREATE TABLE attachment (
  id              Int          NOT NULL auto_increment,
  filename        Varchar(255) NOT NULL,
  initFilename    Varchar(80) NOT NULL,
  code            Varchar(255)  NOT NULL,
  size            Int          NOT NULL,
  path            Varchar(100) NOT NULL,
  uploadUserId    Int          NOT NULL,
  uploadTime      Datetime     NOT NULL,
  PRIMARY KEY  (id),
  KEY INDEX_CODE (code) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS menu;
CREATE TABLE  menu (
  id              Int          NOT NULL auto_increment,
  name            Varchar(40)  NOT NULL,
  icon            Varchar(60),
  link            Varchar(255),
  seq             Int,
  parentId        Int,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dict;
CREATE TABLE dict(
  id              Int           NOT NULL auto_increment,
  tableName       Varchar(40)   NOT NULL,
  fieldName       Varchar(40)   NOT NULL,
  value           Varchar(40)   NOT NULL,
  meaning         Varchar(40)   NOT NULL,
  note            Varchar(255),
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS systemSetting;
CREATE TABLE systemSetting(
  id              Int           NOT NULL auto_increment,
  name            Varchar(40)   NOT NULL,
  value           Varchar(255)  NOT NULL,
  note            Varchar(255),
  category        Varchar(40),
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS actionLog;
CREATE TABLE actionLog(
  id              Int           NOT NULL auto_increment,
  system          Varchar(40)   NOT NULL,
  module          Varchar(40)   NOT NULL,
  action          Varchar(40)   NOT NULL,
  objectId        Varchar(1000),
  note            Varchar(255),
  ip              Varchar(20)   NOT NULL,
  userId          Int           NOT NULL,
  logTime         Datetime      NOT NULL,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS importBatch;
CREATE TABLE importBatch(
  id	          Int           NOT NULL auto_increment,
  module	      Varchar(40)   NOT NULL,
  filename	      Varchar(400)  NOT NULL,
  totalCount	  Int,
  finishCount	  Int,
  failCount	      Int,
  importUserId	  Int           NOT NULL,
  importTime	  Datetime      NOT NULL,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS importLog;
CREATE TABLE importLog(
  id	          Int           NOT NULL auto_increment,
  importBatchId   Int           NOT NULL,
  module	      Varchar(40)   NOT NULL,
  objectId	      Int           NOT NULL,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS license;
CREATE TABLE license(
  id	          Int           NOT NULL auto_increment,
  name      	  Varchar(100)  NOT NULL,
  registerDate	  Date          NOT NULL,
  permitDays	  Int           NOT NULL,
  mac   	      Varchar(40)   NOT NULL,
  licenseCode	  Varchar(255)  NOT NULL,
  PRIMARY KEY  (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;