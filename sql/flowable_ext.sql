-- ----------------------------
-- Table structure for act_http_url
-- ----------------------------
DROP TABLE IF EXISTS `act_http_url`;
CREATE TABLE `act_http_url`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_http_url
-- ----------------------------
INSERT INTO `act_http_url` VALUES ('95b108ef-e280-11e9-bb61-00155ddf8120', 'http://122.112.4.151:6080/auth/admin/realms', '创建域', '创建域');
INSERT INTO `act_http_url` VALUES ('a1c8c0b0-e9b6-11e9-bb61-00155ddf8120', 'http://122.112.4.151:6080/auth/admin/realms/${realm}', '删域', '删除域');
INSERT INTO `act_http_url` VALUES ('a7c96756-c2ed-11e9-9c39-309c23664f9a', 'http://localhost:8180/auth/admin/realms', '', '创建域');

-- ----------------------------
-- Table structure for act_ext_class
-- ----------------------------
DROP TABLE IF EXISTS `act_ext_class`;
CREATE TABLE `act_ext_class`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `className` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fullName` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for act_ext_class
-- ----------------------------
DROP TABLE IF EXISTS `act_ext_class`;
CREATE TABLE `act_ext_class`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `className` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fullName` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ext_class
-- ----------------------------
INSERT INTO `act_ext_class` VALUES ('711aedb9-bf35-11e9-9168-309c23664f9a', '数据库服务', '执行sql', 'com.bootdo.modules.flowable.db.SqlNode');
INSERT INTO `act_ext_class` VALUES ('721ba7a2-d920-11e9-b160-309c23664f9a', '远程命令', '调用远程服务器命令', 'com.bootdo.modules.flowable.shell.ShellNode');

-- ----------------------------
-- Table structure for act_ext_datasource
-- ----------------------------
DROP TABLE IF EXISTS `act_ext_datasource`;
CREATE TABLE `act_ext_datasource`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `dbname` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `driverClassName` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驱动名称',
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'URL',
  `userName` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `userPwd` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_ext_datasource
-- ----------------------------
INSERT INTO `act_ext_datasource` VALUES ('7a838e45-bf3c-11e9-9168-309c23664f9a', '服务器1', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/workflow?useUnicode=true&characterEncoding=utf8', 'root', '123');
INSERT INTO `act_ext_datasource` VALUES ('aa9c4b07-d920-11e9-b160-309c23664f9a', 'shell服务器', '', '122.112.4.152', 'root', 'wangxin.it663.com');
INSERT INTO `act_ext_datasource` VALUES ('b2dca0fd-e282-11e9-bb61-00155ddf8120', '测试数据库1', 'com.mysql.jdbc.Driver', 'jdbc:mysql://10.10.10.111:3306/workflow?useUnicode=true&characterEncoding=utf8', 'test-01', 'test-01');
