use cfw;

truncate table sysUser;
insert into sysUser(departmentId,username,password,realname,roles,status,registerTime) values(1,'admin','42150351027cd4ce42d6065b402b7e92','系统管理员','1',1,now());

truncate table systemSetting;
insert into systemSetting(name,value,note,category) values('uploadRootPath','upload','上传文件根目录','upload');
insert into systemSetting(name,value,note,category) values('maxFileSize','10','最大上传文件大小(M)','upload');
insert into systemSetting(name,value,note,category) values('pathRule','yyyy/mm','目录规则','upload');

truncate table dict;
insert into dict(tableName,fieldName,value,meaning) values('user','status','1','激活');
insert into dict(tableName,fieldName,value,meaning) values('user','status','0','冻结');
insert into dict(tableName,fieldName,value,meaning) values('user','rank','1','员工');
insert into dict(tableName,fieldName,value,meaning) values('user','rank','2','部门领导');
insert into dict(tableName,fieldName,value,meaning) values('user','rank','3','公司领导');
insert into dict(tableName,fieldName,value,meaning) values('area','type','1','省');
insert into dict(tableName,fieldName,value,meaning) values('area','type','2','市');
insert into dict(tableName,fieldName,value,meaning) values('area','type','3','区县');
insert into dict(tableName,fieldName,value,meaning) values('article','status','1','有效');
insert into dict(tableName,fieldName,value,meaning) values('article','status','0','无效');
insert into dict(tableName,fieldName,value,meaning) values('article','approveStatus','0','未审核');
insert into dict(tableName,fieldName,value,meaning) values('article','approveStatus','1','通过审核');
insert into dict(tableName,fieldName,value,meaning) values('article','approveStatus','2','未通过审核');


truncate table menu;
insert into menu(id,name,icon,link,seq,parentId) values(700,'基础数据管理','icon-th','',70000,null);
insert into menu(id,name,icon,link,seq,parentId) values(701,'地区管理',null,'../base/areaList.htm',70001,700);
insert into menu(id,name,icon,link,seq,parentId) values(702,'分类管理',null,'../base/categoryMain.htm',70002,700);

insert into menu(id,name,icon,link,seq,parentId) values(800,'信息发布','icon-font','',80000,null);
insert into menu(id,name,icon,link,seq,parentId) values(801,'频道管理',null,'../article/channelMain.htm',80001,800);
insert into menu(id,name,icon,link,seq,parentId) values(802,'文章管理',null,'../article/articleMain.htm',80002,800);
insert into menu(id,name,icon,link,seq,parentId) values(803,'文章审批',null,'../article/articleApproveMain.htm',80003,800);

insert into menu(id,name,icon,link,seq,parentId) values(900,'系统管理','icon-asterisk','',90000,null);
insert into menu(id,name,icon,link,seq,parentId) values(901,'组织机构管理',null,'../user/departmentMain.htm',90001,900);
insert into menu(id,name,icon,link,seq,parentId) values(902,'用户管理',null,'../user/userMain.htm',90002,900);
insert into menu(id,name,icon,link,seq,parentId) values(903,'角色管理',null,'../privilege/roleList.htm',90003,900);
insert into menu(id,name,icon,link,seq,parentId) values(904,'菜单管理',null,'../system/menuMain.htm',90004,900);
insert into menu(id,name,icon,link,seq,parentId) values(905,'数据字典管理',null,'../system/dictList.htm',90005,900);
insert into menu(id,name,icon,link,seq,parentId) values(906,'系统设置',null,'../system/systemSetting.htm',90006,900);
insert into menu(id,name,icon,link,seq,parentId) values(907,'查看操作日志',null,'../system/actionLogList.htm',90007,900);
insert into menu(id,name,icon,link,seq,parentId) values(908,'查看导入记录',null,'../system/importBatchList.htm',90008,900);
insert into menu(id,name,icon,link,seq,parentId) values(909,'缓存管理',null,'../system/cacheList.htm',90009,900);


truncate table privilege;
insert into privilege(id,name,type,objectId,parentId) values(700,'基础数据管理',1,700,null);
insert into privilege(id,name,type,objectId,parentId) values(701,'地区管理',1,701,700);
insert into privilege(id,name,type,objectId,parentId) values(702,'分类管理',1,702,700);
insert into privilege(id,name,type,objectId,parentId) values(800,'信息发布',1,800,null);
insert into privilege(id,name,type,objectId,parentId) values(801,'频道管理',1,801,800);
insert into privilege(id,name,type,objectId,parentId) values(802,'文章管理',1,802,800);
insert into privilege(id,name,type,objectId,parentId) values(803,'文章审批',1,803,800);
insert into privilege(id,name,type,objectId,parentId) values(900,'系统管理',1,900,null);
insert into privilege(id,name,type,objectId,parentId) values(901,'组织机构管理',1,901,900);
insert into privilege(id,name,type,objectId,parentId) values(902,'用户管理',1,902,900);
insert into privilege(id,name,type,objectId,parentId) values(903,'角色管理',1,903,900);
insert into privilege(id,name,type,objectId,parentId) values(904,'菜单管理',1,904,900);
insert into privilege(id,name,type,objectId,parentId) values(905,'数据字典管理',1,905,900);
insert into privilege(id,name,type,objectId,parentId) values(906,'系统设置',1,906,900);
insert into privilege(id,name,type,objectId,parentId) values(907,'查看操作日志',1,907,900);
insert into privilege(id,name,type,objectId,parentId) values(908,'查看导入记录',1,908,900);
insert into privilege(id,name,type,objectId,parentId) values(909,'缓存管理',1,909,900);

truncate table role;
insert into role(id,name,note) values(1,'系统管理员',null);

truncate table rolePrivilege;
insert into rolePrivilege(roleId,privilegeId) values(1,700);
insert into rolePrivilege(roleId,privilegeId) values(1,701);
insert into rolePrivilege(roleId,privilegeId) values(1,702);
insert into rolePrivilege(roleId,privilegeId) values(1,800);
insert into rolePrivilege(roleId,privilegeId) values(1,801);
insert into rolePrivilege(roleId,privilegeId) values(1,802);
insert into rolePrivilege(roleId,privilegeId) values(1,803);
insert into rolePrivilege(roleId,privilegeId) values(1,900);
insert into rolePrivilege(roleId,privilegeId) values(1,901);
insert into rolePrivilege(roleId,privilegeId) values(1,902);
insert into rolePrivilege(roleId,privilegeId) values(1,903);
insert into rolePrivilege(roleId,privilegeId) values(1,904);
insert into rolePrivilege(roleId,privilegeId) values(1,905);
insert into rolePrivilege(roleId,privilegeId) values(1,906);
insert into rolePrivilege(roleId,privilegeId) values(1,907);
insert into rolePrivilege(roleId,privilegeId) values(1,908);
insert into rolePrivilege(roleId,privilegeId) values(1,909);