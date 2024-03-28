/*
 Navicat Premium Data Transfer

 Source Server         : 个人云
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 82.157.165.65:3307
 Source Schema         : kenn_book

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 27/03/2024 16:59:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_search_rule
-- ----------------------------
DROP TABLE IF EXISTS `book_search_rule`;
CREATE TABLE `book_search_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_id` bigint NULL DEFAULT NULL COMMENT '书源id',
  `charset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码: 默认utf-8',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '源基础url',
  `search_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索书籍的url',
  `search_method` tinyint(1) NULL DEFAULT NULL COMMENT '请求方式 1: get 2: post',
  `search_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索的相关参数',
  `url_encoder` tinyint(1) NULL DEFAULT NULL COMMENT '参数是否需要url编码',
  `param_charset` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数编码 默认utf-8',
  `book_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍列表的规则',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书名的规则',
  `book_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍链接的规则',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取作者名称的规则',
  `last_chapter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取最新章节名称的规则',
  `last_chapter_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取最新章节链接的规则',
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取更新时间的规则',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍封面链接的规则',
  `next_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取下一页的规则',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_search_rule
-- ----------------------------
INSERT INTO `book_search_rule` VALUES (1, 2, 'GBK', 'http://www.shuxiang.la', 'http://www.shuxiang.la/modules/article/search.php', 1, '{\"searchkey\": \"%s\",\"submit\": \"搜索\"}', 1, 'GBK', 'tbody>tr:gt(0)', 'tr>td:eq(0)>a@text', 'tr>td:eq(0)>a@href', 'tr>td:eq(2)@text', 'tr>td:eq(1)>a@text', 'tr>td:eq(1)>a@href', 'tr>td:eq(4)@text', NULL, NULL);
INSERT INTO `book_search_rule` VALUES (6, 7, 'GBK', 'http://www.biqu5200.net', 'http://www.biqu5200.net/modules/article/search.php', 1, '{\"searchkey\": \"%s\"}', 1, 'UTF-8', 'tbody>tr:gt(0)', 'tr>:eq(0)>a@text', 'tr>:eq(0)>a@href', 'tr>:eq(2)@text', 'tr>:eq(1)>a@text', 'tr>:eq(1)>a@href', 'tr>:eq(4)@text', NULL, NULL);
INSERT INTO `book_search_rule` VALUES (7, 8, 'UTF-8', 'https://www.zbcxw.cn', 'https://www.zbcxw.cn/search.php', 1, '{\"key\": \"%s\"}', 1, 'UTF-8', '.leftBox.left>ul>li', '.top.clearfix>h1@text', '.top.clearfix>h1>a@href', '.top.clearfix>.s2@text', NULL, NULL, NULL, '.sCboxBookParL.left>a>img@data-original', '.c >a.aBtn[title=\'下一页\']@href');
INSERT INTO `book_search_rule` VALUES (8, 9, 'GBK', 'https://www.sewenwang.top', 'https://www.sewenwang.top/modules/article/search.php', 2, '{\"searchkey\": \"%s\"}', 1, 'GBK', '.layout.layout2.layout-co18>ul>li:gt(0)', '.s2>a@text', '.s2>a@href', '.s4@text', '.s3>a@text', '.s3>a@href', '.s5@text', NULL, NULL);

-- ----------------------------
-- Table structure for book_source
-- ----------------------------
DROP TABLE IF EXISTS `book_source`;
CREATE TABLE `book_source`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书源名称',
  `sort` tinyint(1) NULL DEFAULT NULL COMMENT '排序',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '1: 已删除 0：未删除',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_source
-- ----------------------------
INSERT INTO `book_source` VALUES (2, '书香小说', 2, 'http://www.shuxiang.la', 0, '2022-05-27 16:59:45', '2022-05-27 16:59:48');
INSERT INTO `book_source` VALUES (7, '笔趣阁5200', 3, 'http://www.biqu5200.net', 0, '2024-03-11 14:57:09', '2024-03-11 14:57:11');
INSERT INTO `book_source` VALUES (8, '星星小说网', 1, 'https://www.zbcxw.cn', 0, '2024-03-12 17:24:47', '2024-03-12 17:24:49');
INSERT INTO `book_source` VALUES (9, '新.涩文', 4, 'https://www.sewenwang.top', 0, '2024-03-26 16:41:36', '2024-03-26 16:41:39');

-- ----------------------------
-- Table structure for browse_history
-- ----------------------------
DROP TABLE IF EXISTS `browse_history`;
CREATE TABLE `browse_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序openid',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书源',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `book_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍链接',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '浏览历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of browse_history
-- ----------------------------
INSERT INTO `browse_history` VALUES (5, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗破苍穹', '书香小说', '天蚕土豆', 'http://www.shuxiang.la/book/17346/', 'http://www.shuxiang.la/files/article/image/17/17333/17333s.jpg', '2024-03-12 17:22:52', '2024-03-12 17:22:52');
INSERT INTO `browse_history` VALUES (6, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '万相之王', '书香小说', '天蚕土豆', 'http://www.shuxiang.la/book/53782/', 'http://www.shuxiang.la/files/article/image/53/53769/53769s.jpg', '2024-03-13 12:39:50', '2024-03-13 12:39:25');
INSERT INTO `browse_history` VALUES (7, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '万界点名册', '笔趣阁5200', '圣骑士的传说', 'http://www.biqu5200.net/138_138037/', 'http://r.m.biqu5200.net/cover/aHR0cDovL2Jvb2tjb3Zlci55dWV3ZW4uY29tL3FkYmltZy8zNDk1NzMvMTAyMjMwNTQ5OS8xODA=', '2024-03-13 13:54:27', '2024-03-13 13:53:43');
INSERT INTO `browse_history` VALUES (8, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '极品仙师', '书香小说', '曾经拥有的方向感', 'http://www.shuxiang.la/book/47832/', 'http://www.shuxiang.la/files/article/image/47/47819/47819s.jpg', '2024-03-13 18:21:47', '2024-03-13 18:21:47');
INSERT INTO `browse_history` VALUES (9, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '开天录', '书香小说', '血红', 'http://www.shuxiang.la/book/8353/', 'http://www.shuxiang.la/files/article/image/8/8340/8340s.jpg', '2024-03-14 06:43:27', '2024-03-13 18:26:21');
INSERT INTO `browse_history` VALUES (10, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '黎明之剑', '笔趣阁5200', '远瞳', 'http://www.biqu5200.net/95_95192/', 'http://r.m.biqu5200.net/cover/aHR0cDovL3FpZGlhbi5xcGljLmNuL3FkYmltZy8zNDk1NzMvMTAxMDQwMDIxNy8xODA=', '2024-03-14 10:53:58', '2024-03-14 10:53:58');
INSERT INTO `browse_history` VALUES (11, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '学霸的黑科技系统', '笔趣阁5200', '晨星LL', 'http://www.biqu5200.net/91_91902/', '', '2024-03-15 00:17:44', '2024-03-14 10:57:21');
INSERT INTO `browse_history` VALUES (12, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '元尊', '笔趣阁5200', '天蚕土豆', 'http://www.biqu5200.net/79_79883/', 'http://r.m.biqu5200.net/cover/aHR0cDovL3N0YXRpYy56b25naGVuZy5jb20vdXBsb2FkL2NvdmVyL2ZlLzk4L2ZlOThiNjhmMDYyN2VlMmIxZmVmMWI0NzhhOTgzNWNjLmpwZWc=', '2024-03-18 14:59:08', '2024-03-14 11:07:39');
INSERT INTO `browse_history` VALUES (13, 'ooKQD40jkVrxB78u59-54JuyGT_E', '亡灵领主弱？你怎么招募陨落神明', '星星小说网', '咸鱼暴起咬人 ', 'https://www.zbcxw.cn/xiaoshuo/281014.html', 'https://www.zbcxw.cn/files/article/image/271/271015/271015s.jpg', '2024-03-14 17:40:40', '2024-03-14 17:40:40');
INSERT INTO `browse_history` VALUES (14, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗罗大陆', '星星小说网', '唐家三少', 'https://www.zbcxw.cn/xiaoshuo/12528.html', 'https://www.zbcxw.cn/files/article/image/2/2529/2529s.jpg', '2024-03-14 18:08:08', '2024-03-14 17:41:04');
INSERT INTO `browse_history` VALUES (15, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗罗大陆4终极斗罗', '星星小说网', '唐家三少', 'https://www.zbcxw.cn/xiaoshuo/10719.html', 'https://www.zbcxw.cn/files/article/image/0/720/720s.jpg', '2024-03-14 17:52:40', '2024-03-14 17:52:40');
INSERT INTO `browse_history` VALUES (16, 'ooKQD40jkVrxB78u59-54JuyGT_E', '冥狱大帝', '星星小说网', '怜之使徒 ', 'https://www.zbcxw.cn/xiaoshuo/291317.html', 'https://www.zbcxw.cn/files/article/image/281/281318/281318s.jpg', '2024-03-14 18:19:21', '2024-03-14 18:19:14');
INSERT INTO `browse_history` VALUES (17, 'ooKQD40jkVrxB78u59-54JuyGT_E', '重生留校躺平，我真不想再卷啊！', '星星小说网', '这肉有毒 ', 'https://www.zbcxw.cn/xiaoshuo/292371.html', 'https://www.zbcxw.cn/files/article/image/282/282372/282372s.jpg', '2024-03-14 18:19:28', '2024-03-14 18:19:28');
INSERT INTO `browse_history` VALUES (18, 'ooKQD40jkVrxB78u59-54JuyGT_E', '盗墓，路飞要拯救世界', '星星小说网', '章鱼的海绵 ', 'https://www.zbcxw.cn/xiaoshuo/303333.html', 'https://www.zbcxw.cn/files/article/image/293/293334/293334s.jpg', '2024-03-14 18:19:40', '2024-03-14 18:19:40');
INSERT INTO `browse_history` VALUES (19, 'ooKQD40jkVrxB78u59-54JuyGT_E', '我欲九天揽月', '星星小说网', '温茶米酒 ', 'https://www.zbcxw.cn/xiaoshuo/262195.html', 'https://www.zbcxw.cn/files/article/image/252/252196/252196s.jpg', '2024-03-14 18:25:15', '2024-03-14 18:25:15');
INSERT INTO `browse_history` VALUES (20, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '学霸的黑科技系统', '星星小说网', '晨星LL', 'https://www.zbcxw.cn/xiaoshuo/14214.html', 'https://www.zbcxw.cn/files/article/image/4/4215/4215s.jpg', '2024-03-18 14:58:40', '2024-03-15 00:17:56');
INSERT INTO `browse_history` VALUES (21, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '元尊', '星星小说网', '天蚕土豆', 'https://www.zbcxw.cn/xiaoshuo/10106.html', 'https://www.zbcxw.cn/files/article/image/0/107/107s.jpg', '2024-03-21 08:00:57', '2024-03-18 14:59:23');
INSERT INTO `browse_history` VALUES (22, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '元尊', '书香小说', '天蚕土豆', 'http://www.shuxiang.la/book/7714/', 'http://www.shuxiang.la/files/article/image/7/7701/7701s.jpg', '2024-03-27 15:01:10', '2024-03-21 08:26:00');
INSERT INTO `browse_history` VALUES (23, 'ooKQD40jkVrxB78u59-54JuyGT_E', '将公司变成我的淫窟', '新.涩文', NULL, 'https://www.sewenwang.top/book/879.html', 'https://www.sewenwang.top/files/article/image/0/879/879s.jpg', '2024-03-27 16:49:54', '2024-03-26 18:58:04');
INSERT INTO `browse_history` VALUES (24, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗破苍穹', '星星小说网', '天蚕土豆', 'https://www.zbcxw.cn/xiaoshuo/12017.html', 'https://www.zbcxw.cn/files/article/image/2/2018/2018s.jpg', '2024-03-27 09:00:07', '2024-03-27 08:59:47');
INSERT INTO `browse_history` VALUES (25, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗破苍穹之千年变', '书香小说', '路人甲乙丙丁戊', 'http://www.shuxiang.la/book/57333/', 'http://www.shuxiang.la/files/article/image/57/57320/57320s.jpg', '2024-03-27 09:00:50', '2024-03-27 09:00:50');
INSERT INTO `browse_history` VALUES (26, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗破苍穹', '笔趣阁5200', '天蚕土豆', 'http://www.biqu5200.net/0_844/', 'http://r.m.biqu5200.net/files/article/image/0/844/844s.jpg', '2024-03-27 09:01:07', '2024-03-27 09:01:07');

-- ----------------------------
-- Table structure for chapter_search_rule
-- ----------------------------
DROP TABLE IF EXISTS `chapter_search_rule`;
CREATE TABLE `chapter_search_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_id` bigint NULL DEFAULT NULL COMMENT '书源id',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '源基础url',
  `chapter_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取章节页面的规则',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍封面链接的规则',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取介绍的规则',
  `chapter_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取章节列表规则',
  `chapter_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取章节名称的规则',
  `chapter_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取章节链接的规则',
  `charset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码: 默认utf-8',
  `next_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一页的规则',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chapter_search_rule
-- ----------------------------
INSERT INTO `chapter_search_rule` VALUES (1, 2, 'http://www.shuxiang.la', NULL, '#fmimg>img@src', '#intro@text', '.mulu_list>li:nth-of-type(1) ~ li', 'li>a@text', 'li>a@href', 'GBK', '.input-group-btn:last-child > a.btn.btn-default:not([disabled])@href<js> baseUrl = baseUrl.substring(0, baseUrl.indexOf(\'/\', 8)); return baseUrl + relativeUrl;</js>');
INSERT INTO `chapter_search_rule` VALUES (4, 7, 'http://www.biqu5200.net', NULL, '#fmimg>img@src', '#intro@text', '#list>dl>dt:nth-of-type(2) ~ dd', 'dd>a@text', 'dd>a@href<js> baseUrl = baseUrl.substring(0, baseUrl.indexOf(\'/\', 8)); return baseUrl + relativeUrl;</js>', 'GBK', '');
INSERT INTO `chapter_search_rule` VALUES (5, 8, 'https://www.zbcxw.cn', '.button.clearfix>a:first-child@href', '.imgBox.left>img@src', 'div.tips.clearfix + div.brief@text', '#newlist>dd', 'dd>a@text', 'dd>a@href<js> baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(\'.html\')); return baseUrl + \'/\' + relativeUrl;</js>', 'UTF-8', NULL);
INSERT INTO `chapter_search_rule` VALUES (6, 9, 'https://www.sewenwang.top', NULL, '', '.desc.xs-hidden@text', '#section-list>li', 'li>a@text', 'li>a@href<js> baseUrl = baseUrl.substring(0, baseUrl.indexOf(\'/\', 8)); return baseUrl + relativeUrl;</js>', 'GBK', NULL);

-- ----------------------------
-- Table structure for explore_search_rule
-- ----------------------------
DROP TABLE IF EXISTS `explore_search_rule`;
CREATE TABLE `explore_search_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_id` bigint NULL DEFAULT NULL COMMENT '书源id',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '源基础url',
  `charset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码: 默认utf-8',
  `category_info` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类json数据',
  `book_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍列表的规则',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书名的规则',
  `book_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍链接的规则',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取作者名称的规则',
  `last_chapter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取最新章节名称的规则',
  `last_chapter_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取最新章节链接的规则',
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取更新时间的规则',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取书籍封面链接的规则',
  `next_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取下一页的规则',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of explore_search_rule
-- ----------------------------
INSERT INTO `explore_search_rule` VALUES (1, 2, 'http://www.shuxiang.la', 'GBK', '[\r\n	{\r\n		\"name\": \"玄幻\",\r\n		\"url\": \"http://www.shuxiang.la/fenlei/1_1/\"\r\n	},\r\n	{\r\n		\"name\": \"科幻\",\r\n		\"url\": \"http://www.shuxiang.la/fenlei/6_1/\"\r\n	},\r\n	{\r\n		\"name\": \"修真\",\r\n		\"url\": \"http://www.shuxiang.la/fenlei/2_1/\"\r\n	},\r\n	{\r\n		\"name\": \"都市\",\r\n		\"url\": \"http://www.shuxiang.la/fenlei/3_1/\"\r\n	},\r\n	{\r\n		\"name\": \"历史\",\r\n		\"url\": \"http://www.shuxiang.la/fenlei/4_1/\"\r\n	},\r\n	{\r\n		\"name\": \"网游\",\r\n		\"url\": \"http://www.shuxiang.la/fenlei/5_1/\"\r\n	}\r\n]', '#mm_14>ul>li:gt(0)', '.sp_2>a@text', '.sp_2>a@href', '.sp_4@text', '.sp_3>a@text', '.sp_3>a@href', '.sp_6@text', NULL, '#pagelink>a.next@href');
INSERT INTO `explore_search_rule` VALUES (7, 8, 'https://www.zbcxw.cn', 'UTF-8', '[\n	{\n		\"name\": \"默认\",\n		\"url\": \"https://www.zbcxw.cn/fenlei/0_0_0_0_0_1.html\"\n	},\n	{\n		\"name\": \"科幻\",\n		\"url\": \"https://www.zbcxw.cn/fenlei/6_0_0_0_0_1.html\"\n	},\n	{\n		\"name\": \"穿越\",\n		\"url\": \"https://www.zbcxw.cn/fenlei/1_0_0_0_0_1.html\"\n	},\n	{\n		\"name\": \"仙侠\",\n		\"url\": \"https://www.zbcxw.cn/fenlei/2_0_0_0_0_1.html\"\n	},\n	{\n		\"name\": \"游戏\",\n		\"url\": \"https://www.zbcxw.cn/fenlei/5_0_0_0_0_1.html\"\n	},\n	{\n		\"name\": \"悬疑\",\n		\"url\": \"https://www.zbcxw.cn/fenlei/7_0_0_0_0_1.html\"\n	}\n]', '.search-order>.con>ul>li', '.freeABoxsCenter.left>.font1>a@text', '.freeABoxsCenter.left>.font1>a@href', '.freeABoxsCenter.left>.font2@text##\\|\\s*.*', '.font4.clearfix>a@text', '.font4.clearfix>a@href', '.font4.clearfix>.s2@text', '.freeABoxsLeft.left>a>img@data-original', '.c >a.aBtn[title=\'下一页\']@href');
INSERT INTO `explore_search_rule` VALUES (8, 9, 'https://www.sewenwang.top', 'GBK', '[\r\n	{\r\n		\"name\": \"都市\",\r\n		\"url\": \"https://www.sewenwang.top/list/1/1.html\"\r\n	},\r\n	{\r\n		\"name\": \"乱伦\",\r\n		\"url\": \"https://www.sewenwang.top/list/2/1.html\"\r\n	},\r\n	{\r\n		\"name\": \"武侠\",\r\n		\"url\": \"https://www.sewenwang.top/list/3/1.html\"\r\n	},\r\n	{\r\n		\"name\": \"校园\",\r\n		\"url\": \"https://www.sewenwang.top/list/4/1.html\"\r\n	},\r\n	{\r\n		\"name\": \"玄幻\",\r\n		\"url\": \"https://www.sewenwang.top/list/5/1.html\"\r\n	},\r\n	{\r\n		\"name\": \"言情\",\r\n		\"url\": \"https://www.sewenwang.top/list/6/1.html\"\r\n	}\r\n]', '.txt-list.txt-list-row5>.item', '.image>a>img@alt', '.image>a@href', '.image>a>img@alt', NULL, NULL, NULL, '', '.pagination.pagination-mga>li[title=\'下一页\']>a@href');

-- ----------------------------
-- Table structure for info_search_rule
-- ----------------------------
DROP TABLE IF EXISTS `info_search_rule`;
CREATE TABLE `info_search_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_id` bigint NULL DEFAULT NULL COMMENT '书源id',
  `base_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '源基础url',
  `charset_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取章节名称的规则',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取详情的规则',
  `next_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一章链接规则',
  `before_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上一章链接规则',
  `next_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一页链接',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of info_search_rule
-- ----------------------------
INSERT INTO `info_search_rule` VALUES (1, 7, 'http://www.biqu5200.net', 'GBK', '.bookname>h1@text', '#content@html', '.bottem1>a:nth-of-type(4)@href<js> baseUrl = baseUrl.substring(0, baseUrl.indexOf(\'/\', 8)); return baseUrl + relativeUrl;</js>', '.bottem1>a:nth-of-type(2)@href<js> baseUrl = baseUrl.substring(0, baseUrl.indexOf(\'/\', 8)); return baseUrl + relativeUrl;</js>', NULL);
INSERT INTO `info_search_rule` VALUES (2, 2, 'http://www.shuxiang.la', 'GBK', '#content>h1@text', '#htmlContent@html##一秒记住【书香小说网 www\\.shuxiang\\.la】，精彩小说无弹窗免费阅读！', '#link-next@href', '#link-preview@href', NULL);
INSERT INTO `info_search_rule` VALUES (3, 8, 'https://www.zbcxw.cn', 'UTF-8', '.neirong>h2@text', '#txt@html', '.zjqh>span>a[type=\'next\']@href<js> baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(\'/\')); return baseUrl + \'/\' + relativeUrl;</js>', '.zjqh>span>a[type=\'prev\']@href<js> baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(\'/\')); return baseUrl + \'/\' + relativeUrl;</js>', NULL);
INSERT INTO `info_search_rule` VALUES (4, 9, 'https://www.sewenwang.top', 'GBK', '.title@text', '#content@html', '.section-opt.m-bottom-opt>a:eq(4)@href<js> baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(\'/\')); return baseUrl + \'/\' + relativeUrl;</js>', '.section-opt.m-bottom-opt>a:eq(0)@href<js> baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(\'/\')); return baseUrl + \'/\' + relativeUrl;</js>', NULL);

-- ----------------------------
-- Table structure for my_bookshelf
-- ----------------------------
DROP TABLE IF EXISTS `my_bookshelf`;
CREATE TABLE `my_bookshelf`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序openid',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名',
  `book_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍链接',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书源',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片链接',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `new_chapter_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最新章节链接',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX ```openid```(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '我的书架表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of my_bookshelf
-- ----------------------------
INSERT INTO `my_bookshelf` VALUES (4, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '开天录', 'http://www.shuxiang.la/book/8353/', '书香小说', 'http://www.shuxiang.la/files/article/image/8/8340/8340s.jpg', '血红', NULL);
INSERT INTO `my_bookshelf` VALUES (5, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '黎明之剑', 'http://www.biqu5200.net/95_95192/', '笔趣阁5200', 'http://r.m.biqu5200.net/cover/aHR0cDovL3FpZGlhbi5xcGljLmNuL3FkYmltZy8zNDk1NzMvMTAxMDQwMDIxNy8xODA=', '远瞳', NULL);
INSERT INTO `my_bookshelf` VALUES (10, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '元尊', 'http://www.shuxiang.la/book/7714/', '书香小说', 'http://www.shuxiang.la/files/article/image/7/7701/7701s.jpg', '天蚕土豆', NULL);

-- ----------------------------
-- Table structure for search_history
-- ----------------------------
DROP TABLE IF EXISTS `search_history`;
CREATE TABLE `search_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序openid',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索信息',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索书源',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX ```openid```(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '搜索历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of search_history
-- ----------------------------
INSERT INTO `search_history` VALUES (4, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '斗罗', NULL, '2024-03-13 10:17:42', '2024-03-13 10:17:42');
INSERT INTO `search_history` VALUES (5, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '万相', NULL, '2024-03-13 12:39:13', '2024-03-13 12:39:13');
INSERT INTO `search_history` VALUES (6, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '万', NULL, '2024-03-13 13:53:09', '2024-03-13 13:53:09');
INSERT INTO `search_history` VALUES (7, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '万界', NULL, '2024-03-13 13:53:30', '2024-03-13 13:53:30');
INSERT INTO `search_history` VALUES (8, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '万界点名册', NULL, '2024-03-13 13:53:39', '2024-03-13 13:53:39');
INSERT INTO `search_history` VALUES (9, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '斗破', NULL, '2024-03-13 13:54:14', '2024-03-13 13:54:14');
INSERT INTO `search_history` VALUES (10, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '天牧', NULL, '2024-03-13 18:15:15', '2024-03-13 18:15:15');
INSERT INTO `search_history` VALUES (12, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '极品仙师', NULL, '2024-03-13 18:21:18', '2024-03-13 18:21:18');
INSERT INTO `search_history` VALUES (13, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '开天', NULL, '2024-03-13 18:26:03', '2024-03-13 18:26:03');
INSERT INTO `search_history` VALUES (14, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '黎明之剑', NULL, '2024-03-14 10:53:40', '2024-03-14 10:53:40');
INSERT INTO `search_history` VALUES (16, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '学霸的黑科技系统', NULL, '2024-03-14 10:57:15', '2024-03-14 10:57:15');
INSERT INTO `search_history` VALUES (17, 'ooKQD47tZ_7m4AyiYl4IJxEBk_PU', '元尊', NULL, '2024-03-14 11:07:31', '2024-03-14 11:07:31');
INSERT INTO `search_history` VALUES (19, 'ooKQD40jkVrxB78u59-54JuyGT_E', '斗破苍穹', NULL, '2024-03-27 08:58:35', '2024-03-27 08:58:35');
INSERT INTO `search_history` VALUES (20, 'ooKQD40jkVrxB78u59-54JuyGT_E', '公司', NULL, '2024-03-27 16:43:05', '2024-03-27 16:43:05');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `openid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'kenn', 'c3c118e521baab677926297f1c31e55f', NULL, '18305571111', '2021-12-07 14:02:34', '2021-12-07 14:02:37');
INSERT INTO `user` VALUES (2, 'prince', 'c3c118e521baab677926297f1c31e55f', NULL, '18305572222', '2021-12-07 14:32:36', '2021-12-07 14:33:00');
INSERT INTO `user` VALUES (4, '不問歸期.', 'wxfile://tmp_df1b791af323ed01f6a10a04dbf10922.jpg', NULL, 'ooKQD40jkVrxB78u59-54JuyGT_E', '2022-12-03 15:39:33', '2022-12-03 15:39:33');
INSERT INTO `user` VALUES (5, 'Sun🍋', 'wxfile://tmp_91ae9799847fe9a40c92962e7ebd548b.jpg', NULL, 'ooKQD4wXWbug9n_ZlIZewCgL5D8c', '2022-12-03 15:41:34', '2022-12-03 15:41:34');

SET FOREIGN_KEY_CHECKS = 1;
