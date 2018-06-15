
CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_cron` varchar(64) NOT NULL,
  `task_name` varchar(32) NOT NULL,
  `task_status` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

# task_status enable(启用) disable(禁用) 两种状态


