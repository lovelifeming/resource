-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.19-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 webapp.student 结构
CREATE TABLE IF NOT EXISTS `student` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_no` varchar(20) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `user_sex` varchar(20) NOT NULL,
  `user_birthday` datetime DEFAULT NULL,
  `user_class` varchar(20) DEFAULT NULL,
  `createtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  PRIMARY KEY (`id`,`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 正在导出表  webapp.student 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` (`id`, `user_no`, `user_name`, `password`, `user_sex`, `user_birthday`, `user_class`, `createtime`, `updatetime`) VALUES
	(1, '108', '曾华', '1313445', '男', '1977-09-01 00:00:00', '95033', '2017-09-17 18:09:51', '2017-09-17 18:09:51'),
	(2, '105', '匡明', 'dad363', '男', '1975-10-02 00:00:00', '95031', '2017-09-17 18:10:29', '2017-09-17 18:10:29'),
	(3, '107', '王丽', '1d4515', '女', '1976-01-23 00:00:00', '95033', '2017-09-17 18:11:03', '2017-09-17 18:11:03'),
	(4, '109', '王芳', '146464', '女', '1975-02-10 00:00:00', '95031', '2017-09-17 18:11:40', '2017-09-17 18:11:40'),
	(5, '101', '李军', '8761367', '男', '1976-02-20 00:00:00', '95033', '2017-09-17 18:12:19', '2017-09-17 18:12:19'),
	(6, '103', '陆君', 'c1336c1', '男', '1974-06-03 00:00:00', '95031', '2017-09-17 18:12:50', '2017-09-17 18:12:50'),
	(7, '110', '李晓明', '313deq', '男', '1979-06-03 00:00:00', '95035', '2017-09-17 18:12:50', '2017-09-17 18:12:50'),
	(8, '113', '杜玉萍', '6498fr', '女', '1980-06-03 00:00:00', '95035', '2017-09-17 18:12:50', '2017-09-17 18:12:50');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
