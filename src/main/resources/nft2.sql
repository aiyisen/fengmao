/*
 Navicat Premium Data Transfer

 Source Server         : 保龄服务器本地
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : 47.104.135.98:3306
 Source Schema         : nft2

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 14/07/2022 20:42:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for con_balance_record
-- ----------------------------
DROP TABLE IF EXISTS `con_balance_record`;
CREATE TABLE `con_balance_record`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `reType` int(0) DEFAULT NULL COMMENT '0充值1提现2购买商品3VIP',
  `orderId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单id',
  `orderType` int(0) DEFAULT NULL COMMENT '订单类型0充值订单1提现申请3商品订单4藏品订单5VIP购买6藏品出售7商品订单退款',
  `totalPrice` decimal(10, 2) DEFAULT NULL COMMENT '总额',
  `tradingChannel` int(0) DEFAULT NULL COMMENT '交易渠道0微信1支付宝2applePay3余额支付4连连pay',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of con_balance_record
-- ----------------------------
INSERT INTO `con_balance_record` VALUES (1, 2, '3', 4, 223.00, 3, 1, '2022-06-23 19:04:17', NULL, NULL, NULL, 1);
INSERT INTO `con_balance_record` VALUES (1, 2, '3', 3, 9.90, 3, 1, '2022-06-23 21:08:10', NULL, NULL, 1, 2);
INSERT INTO `con_balance_record` VALUES (1, 2, '4', 3, 9.90, 3, 1, '2022-06-23 21:40:19', NULL, NULL, 1, 3);
INSERT INTO `con_balance_record` VALUES (1, 2, '5', 3, 9.90, 3, 1, '2022-06-23 21:59:55', NULL, NULL, 1, 4);
INSERT INTO `con_balance_record` VALUES (1, 2, '6', 3, 9.90, 3, 1, '2022-06-23 22:07:16', NULL, NULL, 1, 5);
INSERT INTO `con_balance_record` VALUES (1, 2, '10', 3, 19.00, 3, 1, '2022-06-27 14:30:52', NULL, NULL, 1, 7);
INSERT INTO `con_balance_record` VALUES (1, 2, '11', 3, 19.00, 3, 1, '2022-06-27 14:30:53', NULL, NULL, 1, 8);
INSERT INTO `con_balance_record` VALUES (1, 2, '12', 3, 19.00, 3, 1, '2022-06-27 14:30:54', NULL, NULL, 1, 9);
INSERT INTO `con_balance_record` VALUES (1, 2, '13', 3, 19.00, 3, 1, '2022-06-27 14:30:55', NULL, NULL, 1, 10);
INSERT INTO `con_balance_record` VALUES (1, 2, '14', 3, 19.00, 3, 1, '2022-06-27 14:30:56', NULL, NULL, 1, 11);
INSERT INTO `con_balance_record` VALUES (1, 2, '15', 3, 19.00, 3, 1, '2022-06-27 14:30:57', NULL, NULL, 1, 12);
INSERT INTO `con_balance_record` VALUES (1, 2, '16', 3, 19.00, 3, 1, '2022-06-27 14:30:59', NULL, NULL, 1, 13);
INSERT INTO `con_balance_record` VALUES (1, 2, '17', 3, 19.00, 3, 1, '2022-06-27 14:31:02', NULL, NULL, 1, 14);
INSERT INTO `con_balance_record` VALUES (1, 2, '18', 3, 19.00, 3, 1, '2022-06-27 14:31:03', NULL, NULL, 1, 15);
INSERT INTO `con_balance_record` VALUES (1, 2, '19', 3, 19.00, 3, 1, '2022-06-27 14:31:03', NULL, NULL, 1, 16);
INSERT INTO `con_balance_record` VALUES (1, 2, '20', 3, 19.00, 3, 1, '2022-06-27 14:31:04', NULL, NULL, 1, 17);
INSERT INTO `con_balance_record` VALUES (1, 2, '21', 3, 19.00, 3, 1, '2022-06-27 14:31:05', NULL, NULL, 1, 18);
INSERT INTO `con_balance_record` VALUES (1, 2, '22', 3, 19.00, 3, 1, '2022-06-27 14:31:05', NULL, NULL, 1, 19);
INSERT INTO `con_balance_record` VALUES (1, 2, '23', 3, 19.00, 3, 1, '2022-06-27 14:31:06', NULL, NULL, 1, 20);
INSERT INTO `con_balance_record` VALUES (1, 2, '16', 4, 0.10, 0, 1, '2022-07-02 14:11:26', NULL, NULL, 1, 21);
INSERT INTO `con_balance_record` VALUES (1, 2, '17', 4, 0.10, 0, 1, '2022-07-02 14:21:54', NULL, NULL, 1, 22);
INSERT INTO `con_balance_record` VALUES (1, 2, '25', 4, 0.01, 1, 1, '2022-07-02 15:59:59', NULL, NULL, 1, 23);
INSERT INTO `con_balance_record` VALUES (1, 2, '26', 4, 0.01, 1, 1, '2022-07-02 16:01:34', NULL, NULL, 1, 24);
INSERT INTO `con_balance_record` VALUES (1, 2, '27', 3, 0.01, 0, 1, '2022-07-02 16:20:56', NULL, NULL, 1, 25);
INSERT INTO `con_balance_record` VALUES (1, 2, '28', 3, 0.01, 1, 1, '2022-07-02 16:23:18', NULL, NULL, 1, 26);
INSERT INTO `con_balance_record` VALUES (1, 2, '29', 3, 0.01, 0, 1, '2022-07-03 12:41:36', NULL, NULL, 1, 27);
INSERT INTO `con_balance_record` VALUES (1, 2, '30', 3, 0.01, 1, 1, '2022-07-03 13:51:38', NULL, NULL, 1, 28);
INSERT INTO `con_balance_record` VALUES (1, 2, '31', 3, 0.01, 1, 1, '2022-07-03 13:51:52', NULL, NULL, 1, 29);
INSERT INTO `con_balance_record` VALUES (1, 2, '32', 3, 0.01, 1, 1, '2022-07-03 13:52:05', NULL, NULL, 1, 30);
INSERT INTO `con_balance_record` VALUES (1, 2, '33', 3, 0.01, 1, 1, '2022-07-03 13:52:18', NULL, NULL, 1, 31);
INSERT INTO `con_balance_record` VALUES (1, 2, '34', 3, 0.01, 1, 1, '2022-07-03 13:52:30', NULL, NULL, 1, 32);
INSERT INTO `con_balance_record` VALUES (1, 2, '35', 3, 0.01, 1, 1, '2022-07-03 13:52:44', NULL, NULL, 1, 33);
INSERT INTO `con_balance_record` VALUES (1, 2, '36', 3, 0.01, 1, 1, '2022-07-03 13:52:56', NULL, NULL, 1, 34);
INSERT INTO `con_balance_record` VALUES (1, 2, '37', 3, 0.01, 1, 1, '2022-07-03 13:53:24', NULL, NULL, 1, 35);
INSERT INTO `con_balance_record` VALUES (1, 2, '38', 3, 0.01, 1, 1, '2022-07-03 13:53:43', NULL, NULL, 1, 36);
INSERT INTO `con_balance_record` VALUES (1, 2, '39', 3, 0.01, 1, 1, '2022-07-03 13:54:54', NULL, NULL, 1, 37);
INSERT INTO `con_balance_record` VALUES (1, 2, '40', 3, 0.01, 1, 1, '2022-07-03 13:55:07', NULL, NULL, 1, 38);
INSERT INTO `con_balance_record` VALUES (5, 2, '43', 3, 0.01, 1, 5, '2022-07-04 19:59:40', NULL, NULL, 1, 40);
INSERT INTO `con_balance_record` VALUES (2, 0, '17', 0, 0.01, 0, 2, '2022-07-04 23:08:04', NULL, NULL, 1, 69);
INSERT INTO `con_balance_record` VALUES (3, 0, '18', 0, 0.01, 0, 3, '2022-07-04 23:12:28', NULL, NULL, 1, 70);
INSERT INTO `con_balance_record` VALUES (3, 0, '19', 0, 0.01, 1, 3, '2022-07-04 23:14:11', NULL, NULL, 1, 71);
INSERT INTO `con_balance_record` VALUES (3, 0, '20', 0, 0.01, 0, 3, '2022-07-04 23:17:59', NULL, NULL, 1, 72);
INSERT INTO `con_balance_record` VALUES (3, 2, '47', 3, 0.01, 3, 3, '2022-07-05 15:11:51', NULL, NULL, 1, 73);
INSERT INTO `con_balance_record` VALUES (3, 2, '48', 3, 0.01, 0, 3, '2022-07-05 15:13:05', NULL, NULL, 1, 74);
INSERT INTO `con_balance_record` VALUES (3, 2, '48', 3, 0.01, 0, 3, '2022-07-05 15:13:05', NULL, NULL, 1, 75);
INSERT INTO `con_balance_record` VALUES (3, 2, '49', 3, 0.01, 3, 3, '2022-07-05 16:04:11', NULL, NULL, 1, 76);
INSERT INTO `con_balance_record` VALUES (3, 2, '50', 3, 0.01, 0, 3, '2022-07-05 16:05:04', NULL, NULL, 1, 77);
INSERT INTO `con_balance_record` VALUES (3, 2, '50', 3, 0.01, 0, 3, '2022-07-05 16:05:04', NULL, NULL, 1, 78);
INSERT INTO `con_balance_record` VALUES (3, 0, '21', 0, 0.01, 0, 3, '2022-07-05 16:44:17', NULL, NULL, 1, 79);
INSERT INTO `con_balance_record` VALUES (2, 2, '42', 5, 1.00, 3, 2, '2022-07-05 16:51:54', NULL, NULL, 1, 80);
INSERT INTO `con_balance_record` VALUES (2, 3, '42', 5, 1.00, 3, 2, '2022-07-05 16:51:54', NULL, NULL, 1, 81);
INSERT INTO `con_balance_record` VALUES (3, 2, '44', 5, 1.00, 3, 3, '2022-07-05 16:52:59', NULL, NULL, 1, 82);
INSERT INTO `con_balance_record` VALUES (3, 3, '44', 5, 1.00, 3, 3, '2022-07-05 16:52:59', NULL, NULL, 1, 83);
INSERT INTO `con_balance_record` VALUES (2, 2, '29', 4, 99.00, 3, 2, '2022-07-05 18:44:04', NULL, NULL, 1, 84);
INSERT INTO `con_balance_record` VALUES (2, 2, '29', 4, 99.00, 3, 2, '2022-07-05 18:44:04', NULL, NULL, 1, 85);
INSERT INTO `con_balance_record` VALUES (2, 2, '30', 4, 99.00, 3, 2, '2022-07-07 16:05:07', NULL, NULL, 1, 86);
INSERT INTO `con_balance_record` VALUES (2, 2, '30', 4, 99.00, 3, 2, '2022-07-07 16:05:07', NULL, NULL, 1, 87);
INSERT INTO `con_balance_record` VALUES (5, 2, '32', 4, 0.01, 3, 5, '2022-07-08 20:19:23', NULL, NULL, 1, 88);
INSERT INTO `con_balance_record` VALUES (5, 2, '32', 4, 0.01, 3, 5, '2022-07-08 20:19:23', NULL, NULL, 1, 89);
INSERT INTO `con_balance_record` VALUES (5, 2, '33', 4, 0.01, 3, 5, '2022-07-08 20:19:37', NULL, NULL, 1, 90);
INSERT INTO `con_balance_record` VALUES (5, 2, '33', 4, 0.01, 3, 5, '2022-07-08 20:19:37', NULL, NULL, 1, 91);

-- ----------------------------
-- Table structure for con_integral_record
-- ----------------------------
DROP TABLE IF EXISTS `con_integral_record`;
CREATE TABLE `con_integral_record`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `recordType` int(0) DEFAULT NULL COMMENT '积分记录类型0登录赠送1邀请好友赠送2订单消费3刷一刷',
  `metaCount` int(0) DEFAULT 0 COMMENT '涉及积分总量',
  `orderId` bigint(0) DEFAULT NULL COMMENT '订单id',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of con_integral_record
-- ----------------------------
INSERT INTO `con_integral_record` VALUES (1, 2, 223, 1, 1, '2022-06-23 14:33:54', NULL, NULL, NULL, 1);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-06-27 11:32:46', NULL, NULL, 1, 2);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-06-27 14:37:36', NULL, NULL, 1, 3);
INSERT INTO `con_integral_record` VALUES (3, 3, 14, NULL, 3, '2022-06-27 14:46:18', NULL, NULL, 1, 4);
INSERT INTO `con_integral_record` VALUES (3, 3, 10, NULL, 3, '2022-06-27 15:09:31', NULL, NULL, 1, 5);
INSERT INTO `con_integral_record` VALUES (3, 3, 7, NULL, 3, '2022-06-27 16:40:41', NULL, NULL, 1, 6);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-06-28 12:56:31', NULL, NULL, 1, 7);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-06-28 16:09:59', NULL, NULL, 1, 8);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-06-30 15:06:13', NULL, NULL, 1, 9);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-06-30 16:06:00', NULL, NULL, 1, 10);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-07-01 15:47:17', NULL, NULL, 1, 11);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-07-01 18:50:32', NULL, NULL, 1, 12);
INSERT INTO `con_integral_record` VALUES (1, 0, 30, NULL, 1, '2022-07-02 14:11:02', NULL, NULL, 1, 13);
INSERT INTO `con_integral_record` VALUES (1, 0, 30, NULL, 1, '2022-07-03 12:22:07', NULL, NULL, 1, 14);
INSERT INTO `con_integral_record` VALUES (6, 0, 30, NULL, 6, '2022-07-03 16:23:44', NULL, NULL, 1, 15);
INSERT INTO `con_integral_record` VALUES (7, 0, 30, NULL, 7, '2022-07-04 11:50:29', NULL, NULL, 1, 16);
INSERT INTO `con_integral_record` VALUES (1, 0, 30, NULL, 1, '2022-07-04 19:11:54', NULL, NULL, 1, 17);
INSERT INTO `con_integral_record` VALUES (8, 0, 30, NULL, 8, '2022-07-04 19:58:10', NULL, NULL, 1, 18);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-07-04 20:32:06', NULL, NULL, 1, 19);
INSERT INTO `con_integral_record` VALUES (7, 0, 30, NULL, 7, '2022-07-05 11:13:32', NULL, NULL, 1, 20);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-07-05 15:11:10', NULL, NULL, 1, 21);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-07-05 16:33:50', NULL, NULL, 1, 22);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-07-06 14:23:59', NULL, NULL, 1, 23);
INSERT INTO `con_integral_record` VALUES (6, 0, 30, NULL, 6, '2022-07-06 16:18:22', NULL, NULL, 1, 24);
INSERT INTO `con_integral_record` VALUES (6, 0, 30, NULL, 6, '2022-07-07 09:59:21', NULL, NULL, 1, 25);
INSERT INTO `con_integral_record` VALUES (3, 0, 30, NULL, 3, '2022-07-07 15:37:46', NULL, NULL, 1, 26);
INSERT INTO `con_integral_record` VALUES (2, 0, 30, NULL, 2, '2022-07-07 15:40:59', NULL, NULL, 1, 27);
INSERT INTO `con_integral_record` VALUES (7, 0, 30, NULL, 7, '2022-07-07 19:04:38', NULL, NULL, 1, 28);
INSERT INTO `con_integral_record` VALUES (7, 0, 30, NULL, 7, '2022-07-08 09:01:08', NULL, NULL, 1, 29);
INSERT INTO `con_integral_record` VALUES (11, 0, 30, NULL, 11, '2022-07-08 14:19:14', NULL, NULL, 1, 30);
INSERT INTO `con_integral_record` VALUES (8, 0, 30, NULL, 8, '2022-07-08 20:17:59', NULL, NULL, 1, 31);
INSERT INTO `con_integral_record` VALUES (5, 0, 30, NULL, 5, '2022-07-13 20:51:42', NULL, NULL, 1, 32);

-- ----------------------------
-- Table structure for con_recharge
-- ----------------------------
DROP TABLE IF EXISTS `con_recharge`;
CREATE TABLE `con_recharge`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `orderFingerprint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单指纹唯一标识',
  `tradeNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `payType` int(0) DEFAULT NULL COMMENT '支付方式0微信1支付宝2applePay3余额支付',
  `orderFlag` int(0) DEFAULT 0 COMMENT '充值订单状态0待付款1已付款',
  `totalPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '充值金额',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 0 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of con_recharge
-- ----------------------------
INSERT INTO `con_recharge` VALUES (3, 'f0061417-80bc-4324-982d-b1c11d834af6', NULL, 0, 0, 100.00, 3, '2022-06-27 14:46:10', NULL, NULL, 0, 1);
INSERT INTO `con_recharge` VALUES (3, 'f91b0fe5-2814-4aed-854e-c4fe7d6c70bc', NULL, 0, 0, 400.00, 3, '2022-06-27 14:47:17', NULL, NULL, 0, 2);
INSERT INTO `con_recharge` VALUES (3, '53257347-b33b-49f6-bb71-b1a3f1ea31ec', NULL, 1, 0, 400.00, 3, '2022-06-27 14:47:19', NULL, NULL, 0, 3);
INSERT INTO `con_recharge` VALUES (3, '62c39080-1b62-4620-9c66-d2c5bb25fbf5', NULL, 0, 0, 200.00, 3, '2022-06-27 22:25:47', NULL, NULL, 0, 6);
INSERT INTO `con_recharge` VALUES (3, '6c9b0f2e-67e1-47ca-a362-bcbe1f39e649', NULL, 1, 0, 200.00, 3, '2022-06-27 22:26:57', NULL, NULL, 0, 7);
INSERT INTO `con_recharge` VALUES (1, '1f2bd996fc2b4e60a2edf1a5fcfda085', NULL, 0, 0, 100.00, 1, '2022-06-27 22:59:45', NULL, NULL, 0, 8);
INSERT INTO `con_recharge` VALUES (3, '00367c22dfbb4fc9b9cc1067007d0854', NULL, 0, 0, 100.00, 3, '2022-06-28 16:12:23', NULL, NULL, 0, 9);
INSERT INTO `con_recharge` VALUES (2, '605a5da4a7e349e38c23abaadee448d6', NULL, 0, 0, 100.00, 2, '2022-06-28 16:13:41', NULL, NULL, 0, 10);
INSERT INTO `con_recharge` VALUES (2, '7710158478314b6cbef4840e55259fd3', NULL, 0, 0, 100.00, 2, '2022-06-28 16:15:23', NULL, NULL, 0, 11);
INSERT INTO `con_recharge` VALUES (7, 'd2561b6a0777418c95841487a5590fca', NULL, 0, 0, 200.00, 7, '2022-07-04 11:50:03', NULL, NULL, 0, 12);
INSERT INTO `con_recharge` VALUES (8, '970bfab134204133aff90fb2987ed1e4', NULL, 0, 1, 0.01, 8, '2022-07-04 20:17:50', NULL, NULL, 0, 13);
INSERT INTO `con_recharge` VALUES (8, 'bc645d2d0520424fa4c76db6484441bb', NULL, 1, 1, 0.01, 8, '2022-07-04 20:21:15', NULL, NULL, 0, 14);
INSERT INTO `con_recharge` VALUES (2, '27ef1080ebfa4073aec6eaf9a6a736fd', NULL, 0, 0, 800.00, 2, '2022-07-04 21:43:36', NULL, NULL, 0, 15);
INSERT INTO `con_recharge` VALUES (2, '23327a7710914e5d914eda27a2faec2e', NULL, 1, 0, 800.00, 2, '2022-07-04 21:43:44', NULL, NULL, 0, 16);
INSERT INTO `con_recharge` VALUES (2, 'ff4ac829d8ed4b8896b1f593a1b68ed6', NULL, 0, 1, 0.01, 2, '2022-07-04 23:07:54', NULL, NULL, 0, 17);
INSERT INTO `con_recharge` VALUES (3, '582f1bda0d35411c96ac5b7ec2e6dab3', NULL, 0, 1, 0.01, 3, '2022-07-04 23:12:20', NULL, NULL, 0, 18);
INSERT INTO `con_recharge` VALUES (3, '72f085aab1a341c9b56d58d81111c8ff', NULL, 1, 1, 0.01, 3, '2022-07-04 23:13:32', NULL, NULL, 0, 19);
INSERT INTO `con_recharge` VALUES (3, 'c0dce2260ebc4ea98e551aa1df3a50a6', NULL, 0, 1, 0.01, 3, '2022-07-04 23:17:50', NULL, NULL, 0, 20);
INSERT INTO `con_recharge` VALUES (3, '673c6d121111453d8f8bed6fdde4bbee', NULL, 0, 1, 0.01, 3, '2022-07-05 16:44:11', NULL, NULL, 0, 21);

-- ----------------------------
-- Table structure for con_withdraw
-- ----------------------------
DROP TABLE IF EXISTS `con_withdraw`;
CREATE TABLE `con_withdraw`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id ',
  `orderFingerprint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单指纹唯一标识 ',
  `withdrawTotal` decimal(10, 2) DEFAULT 0.00 COMMENT '提现总额 ',
  `applyFlag` int(0) DEFAULT 0 COMMENT '0待审核1审核通过2审核驳回 ',
  `aliPayIdentity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '支付宝登录账户',
  `aliPayName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '支付宝姓名',
  `allocatedState` int(0) DEFAULT 0 COMMENT '拨付状态0未拨付1已拨付 ',
  `applyCause` varchar(6383) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '原因 ',
  `applyTime` date DEFAULT NULL COMMENT '审核时间 ',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of con_withdraw
-- ----------------------------
INSERT INTO `con_withdraw` VALUES (1, '28075886d1d04beaac7ee9f4d3399372', 0.10, 1, '15140088201', '王振读', 0, NULL, NULL, 1, '2022-07-03 13:50:45', NULL, NULL, 1, 2);

-- ----------------------------
-- Table structure for order_pro_after
-- ----------------------------
DROP TABLE IF EXISTS `order_pro_after`;
CREATE TABLE `order_pro_after`  (
  `orderProductId` bigint(0) DEFAULT NULL COMMENT '权益商城订单id',
  `orderType` int(0) DEFAULT NULL COMMENT '0仅退款1退货退款2换货',
  `orderAfterState` int(0) DEFAULT 0 COMMENT '当前售后订单状态-5审核驳回-1退款驳回0待平台审核1已退款2用户已发货3平台已发货4已完成本轮售后5审核通过',
  `orderReason` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '原因',
  `orderProductState` int(0) DEFAULT 0 COMMENT '0未收到货1已收到货',
  `voucherImgs` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '凭证;拼接',
  `plateTransNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '平台发货物流单号',
  `userTransNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '用户退回物流单号',
  `logisticsOrgName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '物流单号',
  `userIsGet` int(0) DEFAULT 0 COMMENT '用户是否再次收到货0否1是',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_pro_pool_record
-- ----------------------------
DROP TABLE IF EXISTS `order_pro_pool_record`;
CREATE TABLE `order_pro_pool_record`  (
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `proPoolTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `indexPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `orgName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `orgIndexPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `proPrice` decimal(10, 2) DEFAULT NULL,
  `tagCon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `proNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_product
-- ----------------------------
DROP TABLE IF EXISTS `order_product`;
CREATE TABLE `order_product`  (
  `orderFingerprint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单指纹唯一标识',
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `productId` bigint(0) DEFAULT NULL COMMENT '商品id',
  `productType` int(0) DEFAULT NULL,
  `totalPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '总价',
  `proCount` int(0) DEFAULT 1 COMMENT '订单中商品数量',
  `proPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '商品单价',
  `sysAddressId` bigint(0) DEFAULT NULL COMMENT '收货地址id',
  `orderFlag` int(0) DEFAULT 0 COMMENT '订单状态：-2已退款-1已取消0待付款1代发货2待收货3已完成',
  `freight` decimal(10, 2) DEFAULT 0.00 COMMENT '运费',
  `payType` int(0) DEFAULT NULL COMMENT '支付方式0微信1支付宝2applePay',
  `payEndTime` datetime(0) DEFAULT NULL COMMENT '支付截止时间',
  `logisticsOrder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '物流单号',
  `orderRemark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '订单备注',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tradeNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `payTime` datetime(0) DEFAULT NULL,
  `sendTime` datetime(0) DEFAULT NULL,
  `saveTime` datetime(0) DEFAULT NULL,
  `saleAfterActive` int(0) DEFAULT 0 COMMENT '售后是否开启',
  `backLogisticsOrder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `secLogisticsOrder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_product
-- ----------------------------
INSERT INTO `order_product` VALUES ('a3e1d39cba2145b7924d6b0eb3f439ff', 1, 16, 0, 0.01, 1, 199.00, 7, 0, 0.00, 4, '2022-07-13 21:19:58', NULL, '订单备注', 1, '2022-07-13 21:04:58', NULL, NULL, 1, 58, NULL, NULL, NULL, NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for order_product_record
-- ----------------------------
DROP TABLE IF EXISTS `order_product_record`;
CREATE TABLE `order_product_record`  (
  `proType` int(0) DEFAULT NULL COMMENT '商品类型0普通商品1特权专区2兑换专区 ',
  `proTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '商品标题',
  `oldPrice` decimal(10, 2) DEFAULT NULL COMMENT '原价',
  `curPrice` decimal(10, 2) DEFAULT NULL COMMENT '现价 ',
  `proDetail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '详情（富文本） ',
  `banners` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `headerIndex` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '头部图片 ',
  `totalCount` int(0) DEFAULT NULL COMMENT '总量 ',
  `surplusCount` int(0) DEFAULT NULL COMMENT '剩余数量 ',
  `frostCount` int(0) DEFAULT NULL COMMENT '冻结数量/已报名数量（超前申购属性名称） ',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT NULL COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `orderProductId` bigint(0) DEFAULT NULL,
  `storeProductId` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_product_record
-- ----------------------------
INSERT INTO `order_product_record` VALUES (0, '测试商品1', 199.00, 0.01, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/afcf5edf326f461c90c191e46b7ad022.jpeg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/f9c112e475244264b1cd1742535d8960.jpeg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/a2df6bd57bd644aaa17640337bf0b90d.jpeg', 1000, 1000, 0, 1, '2022-07-13 20:55:42', 1, '2022-07-11 13:00:05', 1, 57, 58, 16);

-- ----------------------------
-- Table structure for order_treasure_pool
-- ----------------------------
DROP TABLE IF EXISTS `order_treasure_pool`;
CREATE TABLE `order_treasure_pool`  (
  `orderFingerprint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单指纹唯一标识',
  `teaPoId` bigint(0) DEFAULT NULL COMMENT '藏品id',
  `itemType` int(0) DEFAULT NULL COMMENT '藏品类型0首页藏品1聚合池',
  `curPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '快照单价',
  `totalPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '总价',
  `orderFlag` int(0) DEFAULT 0 COMMENT '1已取消0待付款1发放中2已完成',
  `orderNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '抽签序号',
  `checkFlag` int(0) DEFAULT NULL COMMENT '抽奖标识0未抽中1已抽中',
  `totalCount` int(0) DEFAULT 0 COMMENT '总数量',
  `payType` int(0) DEFAULT NULL COMMENT '支付方式-1积分支付0微信1支付宝2applePay',
  `payEndTime` datetime(0) DEFAULT NULL COMMENT '支付截止时间',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tradeNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `isJoin` int(0) DEFAULT 0 COMMENT '是否为参加申购标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_treasure_pool
-- ----------------------------
INSERT INTO `order_treasure_pool` VALUES ('11afd0c1636e4b73939da29a4648f989', 134, 0, 0.01, 0.01, 2, NULL, NULL, 1, 3, '2022-07-08 20:34:23', 5, '2022-07-08 20:19:23', NULL, '2022-07-08 20:19:23', 1, 32, NULL, 0);
INSERT INTO `order_treasure_pool` VALUES ('753eb08261e54f589e2b79d673c38add', 134, 0, 0.01, 0.01, 2, NULL, NULL, 1, 3, '2022-07-08 20:34:37', 5, '2022-07-08 20:19:37', NULL, '2022-07-08 20:19:37', 1, 33, NULL, 0);

-- ----------------------------
-- Table structure for order_treasure_record
-- ----------------------------
DROP TABLE IF EXISTS `order_treasure_record`;
CREATE TABLE `order_treasure_record`  (
  `orderTreasurePoolId` bigint(0) DEFAULT NULL COMMENT '  /** 订单id */',
  `tType` int(0) DEFAULT NULL COMMENT '  /** 宝贝类型0藏品首发1超前申购 */',
  `treasureTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '  /** 宝贝标题 */',
  `indexImgPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '  /** 主图 */',
  `headImgPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '  /** 头部图片 */',
  `totalCount` int(0) DEFAULT NULL COMMENT '  /** 总量 */',
  `surplusCount` int(0) DEFAULT NULL COMMENT '  /** 剩余数量 */',
  `frostCount` int(0) DEFAULT NULL COMMENT '冻结数量/已报名数量（超前申购属性名称',
  `price` decimal(10, 2) DEFAULT NULL COMMENT '价格(0RMB1META)',
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '作品介绍',
  `sense` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '作品意义',
  `needKnow` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '购买须知',
  `saleTime` date DEFAULT NULL COMMENT '开售时间（藏品首发特有属性',
  `upTime` date DEFAULT NULL COMMENT '报名时间（超前申购特有属性）',
  `checkTime` date DEFAULT NULL COMMENT '抽奖时间（超前申购特有属性）',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT NULL COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `treasureId` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_treasure_record
-- ----------------------------
INSERT INTO `order_treasure_record` VALUES (32, 0, '1号藏品', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 10, 10, 0, 0.01, 'jeishao', 'yiyi', 'goumaixuzhi', '2022-07-07', NULL, NULL, 1, '2022-07-08 20:17:45', NULL, NULL, 1, 31, 134);
INSERT INTO `order_treasure_record` VALUES (33, 0, '1号藏品', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 10, 9, 1, 0.01, 'jeishao', 'yiyi', 'goumaixuzhi', '2022-07-07', NULL, NULL, 1, '2022-07-08 20:17:45', NULL, '2022-07-08 20:19:23', 1, 32, 134);

-- ----------------------------
-- Table structure for order_vip
-- ----------------------------
DROP TABLE IF EXISTS `order_vip`;
CREATE TABLE `order_vip`  (
  `tradeNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '第三方流水',
  `payTotal` decimal(10, 2) DEFAULT NULL COMMENT '支付金额',
  `payType` int(0) DEFAULT NULL COMMENT '0微信1支付宝2applePay3余额',
  `orderFlag` int(0) DEFAULT NULL COMMENT '0待支付1已支付',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uId` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_vip
-- ----------------------------
INSERT INTO `order_vip` VALUES ('3', 14.00, 0, 1, NULL, '2022-07-07 22:19:57', NULL, NULL, 1, 45, 1);
INSERT INTO `order_vip` VALUES ('2', 14.00, 1, 1, NULL, '2022-07-07 22:19:57', NULL, NULL, 1, 46, 1);
INSERT INTO `order_vip` VALUES ('1', 4.00, 3, 1, NULL, '2022-07-07 22:19:57', NULL, NULL, 1, 47, 1);
INSERT INTO `order_vip` VALUES ('155ca482204647be8a826b4f6a3f84ac', 1.00, 0, 0, 7, '2022-07-08 15:31:00', NULL, NULL, 1, 48, 7);

-- ----------------------------
-- Table structure for store_pro_pool
-- ----------------------------
DROP TABLE IF EXISTS `store_pro_pool`;
CREATE TABLE `store_pro_pool`  (
  `proPoolTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '聚合池商品标题 ',
  `indexPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '首图地址 ',
  `orgName` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '企业名称 ',
  `orgIndexPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT '' COMMENT '企业头像 ',
  `proPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '价格 ',
  `tagCon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '标签\';\'分割 ',
  `proNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '藏品编号 ',
  `fromType` int(0) DEFAULT NULL COMMENT '来源0平台1用户',
  `orderFingerprint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单指纹 ',
  `isSale` int(0) DEFAULT 0 COMMENT '是否卖出 ',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `isCompound` int(0) DEFAULT 0 COMMENT '是否为合成',
  `headImgPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `headType` int(0) DEFAULT NULL,
  `sysOrgId` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_pro_pool
-- ----------------------------
INSERT INTO `store_pro_pool` VALUES ('聚合池标题', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/e82063843c664f9085f7974296572851.jpg', '1号发行方2', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 12.00, '潜力股', '017ce615ff0747ab8f3ca0374fa78546', 0, '017ce615ff0747ab8f3ca0374fa78546', 0, 1, '2022-07-13 11:32:36', NULL, NULL, 1, 3, 0, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/8370361caf0b419d8e63bce328c1a1d0.jpg', 0, 7);

-- ----------------------------
-- Table structure for store_pro_pool_col
-- ----------------------------
DROP TABLE IF EXISTS `store_pro_pool_col`;
CREATE TABLE `store_pro_pool_col`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `proPoolId` bigint(0) DEFAULT NULL COMMENT '聚合池商品id',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_pro_pool_read_record
-- ----------------------------
DROP TABLE IF EXISTS `store_pro_pool_read_record`;
CREATE TABLE `store_pro_pool_read_record`  (
  `storeProPoolId` bigint(0) DEFAULT NULL COMMENT '聚合池id',
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT NULL COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_product
-- ----------------------------
DROP TABLE IF EXISTS `store_product`;
CREATE TABLE `store_product`  (
  `proType` int(0) DEFAULT 0 COMMENT '商品类型0普通商品1特权专区2兑换专区',
  `proTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '商品标题',
  `oldPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '原价',
  `curPrice` decimal(10, 2) DEFAULT 0.00 COMMENT '现价',
  `proDetail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '详情“;”按序拼接',
  `headerIndex` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '头部图片',
  `totalCount` int(0) DEFAULT 0 COMMENT '总量',
  `surplusCount` int(0) DEFAULT 0 COMMENT '剩余数量',
  `feight` decimal(10, 2) DEFAULT 0.00 COMMENT '运费',
  `frostCount` int(0) DEFAULT 0 COMMENT '冻结数量/已报名数量（超前申购属性名称）',
  `vipPercent` decimal(10, 2) DEFAULT 1.00 COMMENT 'vip折扣',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `banners` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_product
-- ----------------------------
INSERT INTO `store_product` VALUES (0, '测试商品1', 199.00, 0.01, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/afcf5edf326f461c90c191e46b7ad022.jpeg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/a2df6bd57bd644aaa17640337bf0b90d.jpeg', 1000, 999, 0.00, 1, 1.00, 1, '2022-07-13 20:55:42', 1, '2022-07-11 13:00:05', 1, 16, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/f9c112e475244264b1cd1742535d8960.jpeg');
INSERT INTO `store_product` VALUES (1, '111', 199.00, 1.00, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/2520a528110b4f909680c5e3fa0b7159.jpeg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/30bba3d120b94d5ab1046da38e9a7d6d.jpeg', 1, 1, 0.00, 0, 1.00, 1, '2022-07-13 20:55:48', NULL, NULL, 1, 17, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/c5bb506b866a48c3893a59ac36581a38.jpeg');
INSERT INTO `store_product` VALUES (2, '111', 111.00, 0.01, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/bbe10e8479af45208476453a5ba47485.jpeg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/c7972691a47d4839b6dc5ad4a4515f0c.jpeg', 1000, 1000, 0.00, 0, 1.00, 1, '2022-07-13 20:55:51', NULL, NULL, 1, 18, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/b46f4737fbd54842b5d856a7b352ab7c.jpeg');
INSERT INTO `store_product` VALUES (0, '2', 2.00, 2.00, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/8040851d4b3b447fb30781a4729630ea.png', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/6de303a9a1af4942ad876ba4f0a7412b.png', 2, 2, 2.00, 0, 1.00, 2, '2022-07-13 20:55:54', NULL, NULL, 1, 19, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/44541f0415444767b3bf897e1cfa5e65.png');
INSERT INTO `store_product` VALUES (0, '2', 2.00, 2.00, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/8040851d4b3b447fb30781a4729630ea.png', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/6de303a9a1af4942ad876ba4f0a7412b.png', 2, 2, 2.00, 0, 1.00, 1, '2022-07-13 20:55:59', NULL, NULL, 1, 20, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/44541f0415444767b3bf897e1cfa5e65.png');
INSERT INTO `store_product` VALUES (0, '2', 2.00, 2.00, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/8040851d4b3b447fb30781a4729630ea.png', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/6de303a9a1af4942ad876ba4f0a7412b.png', 2, 2, 2.00, 0, 1.00, 1, '2022-07-13 20:55:56', NULL, NULL, 1, 21, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/44541f0415444767b3bf897e1cfa5e65.png');

-- ----------------------------
-- Table structure for store_product_template
-- ----------------------------
DROP TABLE IF EXISTS `store_product_template`;
CREATE TABLE `store_product_template`  (
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT NULL COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `productId` bigint(0) DEFAULT NULL,
  `needId` bigint(0) DEFAULT NULL,
  `needType` int(0) DEFAULT NULL COMMENT '0藏品首发1超前申购',
  `needCount` int(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_product_template
-- ----------------------------
INSERT INTO `store_product_template` VALUES (1, '2022-07-11 12:58:25', NULL, NULL, 1, 5, 17, 134, 0, 1);

-- ----------------------------
-- Table structure for store_treasure
-- ----------------------------
DROP TABLE IF EXISTS `store_treasure`;
CREATE TABLE `store_treasure`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tType` int(0) DEFAULT NULL COMMENT '藏品类型0藏品首发1超前申购 2合成藏品',
  `rType` int(0) DEFAULT NULL COMMENT '记录类型0-721 1-155',
  `linkAddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '合约地址',
  `tNum` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '藏品编号 ',
  `treasureTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '藏品标题 ',
  `indexImgPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '详情主图\';\'号按序拼接-作品故事 ',
  `headImgPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '头部图 ',
  `headType` int(0) DEFAULT NULL COMMENT '头部类型0:jpg/jpeg/png 1:gif; 2:mp4/3gp/rmvb; 3:abc/fbx/dae/obj/bvh/dxf/psk/stl/ply/x3d ',
  `totalCount` int(0) DEFAULT 0 COMMENT '总量 ',
  `surplusCount` int(0) DEFAULT 0 COMMENT '剩余数量 ',
  `frostCount` int(0) DEFAULT 0 COMMENT '冻结数量/已报名数量（超前申购属性名称） ',
  `price` decimal(10, 2) DEFAULT 0.00 COMMENT '价格(0RMB1META) ',
  `zeroPrice` decimal(10, 2) DEFAULT 0.00,
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '作品介绍 ',
  `sense` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '作品意义 ',
  `needKnow` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '购买须知 ',
  `saleTime` datetime(0) DEFAULT NULL COMMENT '开售时间（藏品首发特有属性 ',
  `upTime` datetime(0) DEFAULT NULL COMMENT '报名开始时间（超前申购特有属性） ',
  `downTime` datetime(0) DEFAULT NULL COMMENT '报名截止时间（超亲申购特有属性） ',
  `checkTime` datetime(0) DEFAULT NULL COMMENT '抽奖时间（超前申购特有属性） ',
  `couldCompound` int(0) DEFAULT 0 COMMENT '是否允许合成0否1是 ',
  `tagCon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `beforeRule` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `sysOrgId` bigint(0) DEFAULT NULL COMMENT '发行方id',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `ruleCount` int(0) DEFAULT 0 COMMENT '限购数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_treasure
-- ----------------------------
INSERT INTO `store_treasure` VALUES (134, 0, 0, NULL, '001', '1号藏品', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 0, 10, 8, 2, 0.01, 0.00, 'jeishao', 'yiyi', 'goumaixuzhi', '2022-07-07 20:17:23', NULL, NULL, NULL, 0, NULL, '1123', 7, 1, '2022-07-08 20:17:45', NULL, '2022-07-08 20:19:37', 1, 'xiangqing', 0);
INSERT INTO `store_treasure` VALUES (135, 0, 1, NULL, '1526234', '卡比', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/80318f7fd31744a8a9d78374615b2d68.png', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/cc628d98166944609536e6ca3fba2250.png', 0, 200, 200, 0, 200.00, 0.00, NULL, NULL, '<p>1. 超前申购的比例为10-50%</p><p>2.用户在进行超前申购抽签时，需要消耗一定数量meta</p><p>3.用户中签后，方可进行藏品的购买页面付款购买</p>', '2022-07-30 00:00:00', NULL, NULL, NULL, 0, NULL, NULL, 7, 1, '2022-07-12 18:35:39', NULL, NULL, 1, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/e096ad5088e84d27bcd9d750c6ff81c1.png', 0);

-- ----------------------------
-- Table structure for store_treasure_check_user
-- ----------------------------
DROP TABLE IF EXISTS `store_treasure_check_user`;
CREATE TABLE `store_treasure_check_user`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id ',
  `storeTreasureId` bigint(0) DEFAULT NULL COMMENT '藏品id ',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_treasure_needed
-- ----------------------------
DROP TABLE IF EXISTS `store_treasure_needed`;
CREATE TABLE `store_treasure_needed`  (
  `storeTreasureId` bigint(0) DEFAULT NULL COMMENT '被合成藏品id',
  `needStoreTreasureId` bigint(0) DEFAULT NULL COMMENT '所需藏品id',
  `needCount` int(0) DEFAULT 1 COMMENT '所需数量',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_address
-- ----------------------------
DROP TABLE IF EXISTS `sys_address`;
CREATE TABLE `sys_address`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id */',
  `provName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '省级/直辖市id',
  `cityName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '市级id',
  `conName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '县级id',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '详细',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `isDefault` int(0) DEFAULT 0,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_address
-- ----------------------------
INSERT INTO `sys_address` VALUES (1, '北京市', '北京市', '东城区', '01栋', 1, '2022-07-13 20:57:31', NULL, NULL, 1, 7, 1, '15140088201', '屌丝');

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `areaName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `pid` int(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_banner
-- ----------------------------
DROP TABLE IF EXISTS `sys_banner`;
CREATE TABLE `sys_banner`  (
  `bannerPath` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '轮播图片路径',
  `bannerType` int(0) DEFAULT NULL,
  `orderSum` int(0) DEFAULT NULL COMMENT '序号',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_banner
-- ----------------------------
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/4faa49b844824fe694f33e115df986cd.jpeg', 0, 1, 1, '2022-07-11 12:55:07', NULL, NULL, 1, 10);
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/47ddfa51a4b6413cb8a54a26b09a292a.jpeg', 0, 2, 1, '2022-07-11 12:55:19', NULL, NULL, 1, 11);
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/8dc2c8f4324d40a29c424b715f91abc1.jpeg', 0, 3, 1, '2022-07-11 12:55:31', NULL, NULL, 1, 12);
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/59f48b645b7a48f69356e531e1b1c62b.jpeg', 0, 4, 1, '2022-07-11 12:55:45', NULL, NULL, 1, 13);
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/ae8cee2af2224d659be2d49b4f99ebc7.jpeg', 1, 1, 1, '2022-07-11 12:55:58', NULL, NULL, 1, 14);
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/9748962252d243568ae850c9e2a168e7.jpeg', 1, 2, 1, '2022-07-11 12:56:08', NULL, NULL, 1, 15);
INSERT INTO `sys_banner` VALUES ('https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/6a1ae917468f4b739d8a07c0e76d3255.jpeg', 1, 3, 1, '2022-07-11 12:56:38', NULL, NULL, 1, 17);

-- ----------------------------
-- Table structure for sys_business
-- ----------------------------
DROP TABLE IF EXISTS `sys_business`;
CREATE TABLE `sys_business`  (
  `userName` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '姓名',
  `orgName` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '企业',
  `contactJob` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '联系人职务',
  `tell` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '联系方式',
  `eMail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '电子邮箱',
  `wxCode` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '微信号',
  `ipTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '拟合作ip信息',
  `ipRelation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '您与合作ip关系',
  `fromWhere` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '从何处获取的入驻申请链接',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_collect
-- ----------------------------
DROP TABLE IF EXISTS `sys_collect`;
CREATE TABLE `sys_collect`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `date` datetime(0) DEFAULT NULL,
  `totalCount` int(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_collect
-- ----------------------------
INSERT INTO `sys_collect` VALUES (15, '2022-07-07 00:00:00', 31);
INSERT INTO `sys_collect` VALUES (16, '2022-07-08 00:00:00', 6);
INSERT INTO `sys_collect` VALUES (17, '2022-07-08 00:00:00', 0);
INSERT INTO `sys_collect` VALUES (18, '2022-07-09 00:00:00', 11);
INSERT INTO `sys_collect` VALUES (19, '2022-07-09 00:00:02', 0);
INSERT INTO `sys_collect` VALUES (20, '2022-07-10 00:00:00', 14);
INSERT INTO `sys_collect` VALUES (21, '2022-07-11 00:00:00', 15);
INSERT INTO `sys_collect` VALUES (22, '2022-07-12 00:00:00', 20);
INSERT INTO `sys_collect` VALUES (23, '2022-07-13 00:00:00', 16);
INSERT INTO `sys_collect` VALUES (24, '2022-07-13 00:00:00', 0);

-- ----------------------------
-- Table structure for sys_dictonary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictonary`;
CREATE TABLE `sys_dictonary`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '字典值名称-废弃',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '别名',
  `threshold` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '阈值',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `dicTitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dictonary
-- ----------------------------
INSERT INTO `sys_dictonary` VALUES (1, '聚合池藏品标签', 'pool_tag', '潜力股', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (2, '平台售后收货地址', 'order_back_address', '13800000000;宝龄国际;辽宁沈阳', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (3, '权益商城退货理由', 'order_product_back_reason', '七天无理由', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (4, '邀请注册赠送积分', 'login_send_count ', '30', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (5, 'vip聚合池刷一刷赠送概率≤1', 'vip_pol_meta_multiple  ', '1', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (6, '聚合池非会员每日浏览量', 'no_vip_pol_max_count', '300', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (7, '聚合池刷一刷赠送最大积分', 'pool_send_max_meta', '20', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (8, '非会员聚合池刷一刷赠送积分概率', 'pool_send_percent ', '0.1', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (9, '邀请用户注册赠送积分', 'invite_user_integer', '30', 1, '2022-07-04 20:32:06', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (10, '聚合池藏品标签', 'pool_tag', '科技', 1, '2022-07-04 20:32:07', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (12, '权益商城退货理由', 'order_product_back_reason', '少用优惠', 1, '2022-07-04 20:32:07', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (13, 'VIP剩余总量', 'vip_total_count', '27', 1, '2022-07-05 16:52:59', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (14, 'VIP价格', 'vip_price', '1', 1, '2022-07-04 21:52:36', NULL, NULL, 1, NULL);
INSERT INTO `sys_dictonary` VALUES (15, 'VIP有效时间（天）', 'vip_days', '30', NULL, '2022-07-04 20:54:35', NULL, NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_help
-- ----------------------------
DROP TABLE IF EXISTS `sys_help`;
CREATE TABLE `sys_help`  (
  `queTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '标题',
  `queAns` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '内容（富文本）',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_idea
-- ----------------------------
DROP TABLE IF EXISTS `sys_idea`;
CREATE TABLE `sys_idea`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `ideaContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '意见反馈内容',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` bigint(0) DEFAULT 0,
  `type` int(0) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `sortNumber` int(0) DEFAULT NULL,
  `state` int(0) DEFAULT 1,
  `identifying` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '权限标识',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 2, '首页', 'main', '', NULL, 2, -1, NULL, 1, '2022-06-24 16:45:26', NULL, NULL);
INSERT INTO `sys_menu` VALUES (2, 0, 1, '系统管理', 'system', '', NULL, 3, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, 0, 1, '用户管理', 'user', '', NULL, 4, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (4, 0, 1, '虚拟商品', 'storeTreasure', '', '', 5, 1, NULL, 1, '2022-06-28 10:40:18', NULL, NULL);
INSERT INTO `sys_menu` VALUES (5, 0, 1, '权益商城', 'mall', '', NULL, 6, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (6, 0, 1, '订单管理', 'order', '', NULL, 7, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (7, 0, 1, '平台管理', 'notice', '', NULL, 8, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (8, 0, 1, '统计管理', 'statistics', '', NULL, 9, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (9, 2, 2, '管理员管理', 'userconfig', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (10, 2, 2, '菜单管理', 'menuconfig', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (11, 2, 2, '角色管理', 'roleconfig', '', NULL, 2, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (12, 2, 2, '字典管理', 'dictconfig', '', NULL, 3, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (13, 3, 2, '用户信息', 'userInfo', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (14, 4, 2, '藏品信息', 'storeTreasureInfo', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (15, 4, 2, '藏品合成', 'storeCompose', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (16, 5, 2, '普通专区', 'common', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (17, 5, 2, '特权专区', 'privilege', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (18, 5, 2, '兑换专区', 'exchange', '', NULL, 2, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (19, 6, 2, '聚合池藏品订单', 'goodsOrder', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (20, 6, 2, '售后订单', 'saleAfterOrders', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (21, 7, 2, '公告信息', 'noticeInfo', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (22, 7, 2, '平台信息', 'basicInfo', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (23, 8, 2, '超前申购统计', 'beforeCollect', '', NULL, 0, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (24, 8, 2, '权益商城统计', 'productCollect', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (25, 8, 2, '注册用户统计', 'userCollect', '', NULL, 1, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (26, 8, 2, '访问量统计', 'findCollect', '', NULL, 3, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (27, 8, 2, '藏品统计', 'treasureCollect', '', NULL, 4, 1, NULL, 1, '2022-06-23 20:31:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (28, 6, 2, '权益商城订单', 'mallOrder', '', '', 2, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (29, 7, 2, '反馈建议', 'feedback', '', '', 3, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (30, 7, 2, '商务合作记录', 'business', '', '', 4, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (31, 4, 2, '聚合池', 'storeProPool', '', '', 5, -1, NULL, NULL, '2022-06-28 10:45:06', NULL, NULL);
INSERT INTO `sys_menu` VALUES (32, 4, 2, '聚合池', 'storeProPool', '', '', 4, -1, NULL, NULL, '2022-06-28 10:45:12', NULL, NULL);
INSERT INTO `sys_menu` VALUES (33, 4, 2, '聚合池', 'storeProPool', '', '', 3, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (34, 3, 2, '用户提现管理', 'conWithdraw', '', '', 2, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (35, 7, 2, '轮播图管理', 'banner', '', '', 5, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (36, 7, 2, '发行方管理', 'sysOrg', '', '', 6, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (37, 8, 2, 'VIP订单记录', 'orderCollect', '', '', 6, 1, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `msgType` int(0) DEFAULT NULL COMMENT '消息类型0系统通知1推送消息',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '消息内容',
  `msgTitle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '消息标题',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_message
-- ----------------------------
INSERT INTO `sys_message` VALUES (0, '<p>hello</p>', 'hello', 1, NULL, NULL, NULL, 1, 17);

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `orgName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '发行方名称',
  `orgImg` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '发行方头像地址',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci COMMENT = '发行方' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES (7, '1号发行方2', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/d23172f41b004c96a0ef0c1e8d31b40e.jpg', 1, '2022-07-08 20:47:25', 1, NULL, 1);
INSERT INTO `sys_org` VALUES (8, '测试发行方1', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/6e80bfa8a13f498196b662c6efeeb0bf.png', NULL, '2022-07-13 11:00:08', 1, NULL, 1);

-- ----------------------------
-- Table structure for sys_plate_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_plate_config`;
CREATE TABLE `sys_plate_config`  (
  `userAgreement` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '用户协议（富文本）',
  `privacyPolicy` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '隐私协议（富文本）',
  `mobilePolicy` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '运营商协议（富文本）',
  `newbieGuide` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '新手指南（富文本）',
  `aboutUs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '关于我们（富文本）',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vipRules` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `vipEquity` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `inviteInfo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `treaNeedKnow` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `beforeNeedKnow` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  `beforeRule` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_plate_config
-- ----------------------------
INSERT INTO `sys_plate_config` VALUES ('<p><font color=\"#ffffff\">asd</font></p>', '<p><font color=\"#ffffff\">asd</font></p>', '<p><font color=\"#eeece0\">asd</font></p>', '<h3><font color=\"#ffffff\"><b></b><span style=\"font-size: 20px; display: inline !important;\" id=\"bxrkr\">什么是数字藏品？</span></font></h3><div><span style=\"font-size: 20px; display: inline !important;\"><font color=\"#ffffff\">数字藏品是一种非同质化代币</font></span></div>', '<p><font size=\"7\" color=\"#ffffff\">X星云致力于构建中国web3.0时代的去中心化基础设施。在医、食、住、行、娱、教六大领域深耕发展。建立中国的数字电商基础设施。</font></p>', 1, '2022-07-07 17:33:14', 1, '2022-07-07 17:33:15', 1, 1, '持有vip会员权益的用户，能够享受到平台的实体商品权益。', '成为平台会员，享受特权', 'asdf', '<p>1. 超前申购的比例为10-50%</p><p>2.用户在进行超前申购抽签时，需要消耗一定数量meta</p><p>3.用户中签后，方可进行藏品的购买页面付款购买</p>', '<p>1. 超前申购的比例为10-50%</p><p>2.用户在进行超前申购抽签时，需要消耗一定数量meta</p><p>3.用户中签后，方可进行藏品的购买页面付款购买</p>', '<p><font color=\"#ffffff\">1. 超前申购的比例为10-50%</font></p><p><font color=\"#ffffff\">2.用户在进行超前申购抽签时，需要消耗一定数量meta</font></p><p><font color=\"#ffffff\">3.用户中签后，方可进行藏品的购买页面付款购买</font></p>');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL,
  `sortNumber` int(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (NULL, '2022-06-23 19:47:09', NULL, NULL, 1, 1, '系统管理员', '最高权限', 1);
INSERT INTO `sys_role` VALUES (1, '2022-06-27 12:15:43', NULL, NULL, 1, 2, '管理员', NULL, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `roleId` int(0) DEFAULT NULL,
  `menuId` int(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 476 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 408, 1, 2);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 409, 1, 9);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 410, 1, 10);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 411, 1, 11);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 412, 1, 12);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 413, 1, 3);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 414, 1, 13);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 415, 1, 34);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 416, 1, 4);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 417, 1, 14);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 418, 1, 15);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 419, 1, 33);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 420, 1, 5);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 421, 1, 16);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 422, 1, 17);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 423, 1, 18);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 424, 1, 6);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 425, 1, 19);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 426, 1, 20);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 427, 1, 28);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 428, 1, 7);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 429, 1, 21);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 430, 1, 22);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 431, 1, 29);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 432, 1, 30);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 433, 1, 35);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 434, 1, 36);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 435, 1, 8);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 436, 1, 23);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 437, 1, 24);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 438, 1, 25);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 439, 1, 26);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 440, 1, 27);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 441, 1, 37);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 442, 2, 2);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 443, 2, 9);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 444, 2, 10);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 445, 2, 11);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 446, 2, 12);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 447, 2, 3);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 448, 2, 13);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 449, 2, 34);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 450, 2, 4);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 451, 2, 14);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 452, 2, 15);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 453, 2, 33);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 454, 2, 5);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 455, 2, 16);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 456, 2, 17);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 457, 2, 18);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 458, 2, 6);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 459, 2, 19);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 460, 2, 20);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 461, 2, 28);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 462, 2, 7);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 463, 2, 21);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 464, 2, 22);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 465, 2, 29);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 466, 2, 30);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 467, 2, 35);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 468, 2, 36);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 469, 2, 8);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 470, 2, 23);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 471, 2, 24);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 472, 2, 25);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 473, 2, 26);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 474, 2, 27);
INSERT INTO `sys_role_menu` VALUES (NULL, NULL, NULL, NULL, 1, 475, 2, 37);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `appleId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '苹果id',
  `wxOpenId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '微信id',
  `qqOpenId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT 'qqID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '账户名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '电子邮箱',
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '密码',
  `linkAddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '区块链地址',
  `realName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '真实姓名',
  `idCard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '身份证号',
  `idFirstPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '身份证正面图片',
  `idSecPath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '身份证背面图片',
  `isTrue` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT '0' COMMENT '是否实名0未实名1已实名',
  `metaCount` bigint(0) DEFAULT 0 COMMENT '积分总量',
  `balance` decimal(10, 2) DEFAULT 0.00 COMMENT '余额（单位元）',
  `userIndex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '用户唯一标识',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `operationPass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '操作密码',
  `isVip` int(0) DEFAULT 0 COMMENT '作废无意义',
  `vipEndTime` datetime(0) DEFAULT NULL COMMENT 'vip截止时间',
  `headImg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('111', NULL, NULL, NULL, '15140088201', NULL, NULL, '592341431754948608', NULL, NULL, NULL, NULL, NULL, 90, 1971.39, '592341', 1, '2022-06-23 21:02:44', NULL, '2022-06-23 14:33:53', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, '111111', '15542914939', NULL, NULL, '592465550966259712', '吕金状', '21142119940611621X', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/8db155f9f6e9480da0e2e7943d47ed3a.jpg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/55ddc092ae7f42d18467769f7fcf67e2.jpg', '1', 210, 3282.00, '592465', 2, '2022-07-04 21:12:24', NULL, NULL, 1, 2, NULL, 0, '2022-08-04 16:51:54', 'http://39.107.247.125:8090/local/download?path=2022-06-27/593765086674288640.jpg');
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, '哈哈', '13674282512', NULL, NULL, '592475971647438848', NULL, NULL, NULL, NULL, '0', 241, 99999.02, '592475', 3, '2022-06-23 22:07:59', NULL, NULL, 1, 3, NULL, 0, '2022-08-04 16:52:59', 'http://39.107.247.125:8090/local/download?path=2022-06-27/593812274032934912.jpg');
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '13654974713', NULL, NULL, '594272819865452544', NULL, NULL, NULL, NULL, '0', 0, 0.00, '594272', 4, '2022-06-28 21:08:01', NULL, NULL, 1, 4, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, '读少', '18509862060', NULL, NULL, '595311794923241472', '王振读', '211321199510208653', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/f0f955982dfb4dd2a093997ada4f467d.jpg', 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/2444fb7155b54db282a3afd1bb647e9e.jpg', '1', 30, 999.98, '595311', 5, '2022-07-08 20:19:19', NULL, NULL, 1, 5, NULL, 0, NULL, 'https://nft-xingyun.oss-cn-hangzhou.aliyuncs.com/183feaa2147d497697ade93fb62740a3.jpg');
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '15302187322', NULL, NULL, '596013009382408192', NULL, NULL, NULL, NULL, '0', 90, 500000.00, '596013', 6, '2022-07-03 16:22:55', NULL, NULL, 1, 6, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '15008287537', NULL, NULL, '596306601049063424', NULL, NULL, NULL, NULL, '0', 120, 800000.00, '596306', 7, '2022-07-04 11:49:33', NULL, NULL, 1, 7, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '17602492290', NULL, NULL, '596417741242499072', NULL, NULL, NULL, NULL, '0', 60, 0.02, '596417', 8, '2022-07-04 21:29:35', NULL, NULL, 1, 8, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '15010372044', NULL, NULL, '597467533028622336', NULL, NULL, NULL, NULL, '0', 0, 0.00, '597467', 9, '2022-07-07 16:42:40', NULL, NULL, 1, 9, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '155429139', NULL, NULL, '597469284695801856', NULL, NULL, NULL, NULL, '0', 0, 0.00, '597469', 10, '2022-07-07 16:49:38', NULL, NULL, 1, 10, NULL, 0, NULL, NULL);
INSERT INTO `sys_user` VALUES (NULL, NULL, NULL, NULL, '13269938635', NULL, NULL, '597725765550735360', NULL, NULL, NULL, NULL, '0', 30, 0.00, '597725', 11, '2022-07-08 09:48:48', NULL, NULL, 1, 11, NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_admin`;
CREATE TABLE `sys_user_admin`  (
  `adminName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '管理员名称',
  `adminMobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '管理员手机号',
  `adminAccount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '管理员账号',
  `adminPass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '管理员密码',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_admin
-- ----------------------------
INSERT INTO `sys_user_admin` VALUES ('admin', '15140088201', 'admin', 'jQhYX6kNqRTvTr1WayHYig==', 1, '2022-06-23 19:46:43', NULL, '2022-07-04 09:49:13', 1, 1);
INSERT INTO `sys_user_admin` VALUES ('zhx', '15140105036', 'zhx', 'jQhYX6kNqRTvTr1WayHYig==', 1, '2022-06-27 12:29:30', 1, '2022-06-27 12:29:30', 1, 2);

-- ----------------------------
-- Table structure for sys_user_backpack
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_backpack`;
CREATE TABLE `sys_user_backpack`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `sTreasureId` bigint(0) DEFAULT NULL COMMENT '藏品id',
  `treasureFrom` int(0) DEFAULT NULL COMMENT '来源0藏品1聚合池',
  `orderFingerprint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '订单指纹',
  `finType` int(0) DEFAULT NULL COMMENT '记录类型：1赠送2获赠3出售4购买6合成',
  `createId` bigint(0) DEFAULT NULL COMMENT '创建者ID',
  `createTime` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updateId` bigint(0) DEFAULT NULL COMMENT '修改者id',
  `updateTime` datetime(0) DEFAULT NULL COMMENT '修改时间',
  `state` int(0) DEFAULT 1 COMMENT '-1逻辑删除0禁用1正常',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `orderTreasurePoolId` bigint(0) DEFAULT NULL,
  `beforeUserId` bigint(0) DEFAULT NULL,
  `afterUserId` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_backpack
-- ----------------------------
INSERT INTO `sys_user_backpack` VALUES (5, 134, 0, '11afd0c1636e4b73939da29a4648f989', 4, 5, '2022-07-08 20:19:23', NULL, NULL, 1, 8, 32, 0, 5);
INSERT INTO `sys_user_backpack` VALUES (5, 134, 0, '753eb08261e54f589e2b79d673c38add', 4, 5, '2022-07-08 20:19:37', NULL, NULL, 1, 9, 33, 0, 5);

-- ----------------------------
-- Table structure for sys_user_invite_link
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_invite_link`;
CREATE TABLE `sys_user_invite_link`  (
  `uId` bigint(0) DEFAULT NULL COMMENT '用户id',
  `seUId` bigint(0) DEFAULT NULL COMMENT '被邀请用户id',
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `integralCount` bigint(0) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `userId` bigint(0) DEFAULT NULL COMMENT '用户编号',
  `roleId` bigint(0) DEFAULT NULL COMMENT '角色编号'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 1);

SET FOREIGN_KEY_CHECKS = 1;
