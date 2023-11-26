INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (1, '信息管理', 'UserFilled', 0, 1, '/home/info', '1', 'M', '', '2023-11-05 14:56:29', '2023-11-05 14:56:31', '信息管理目录');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (2, '课程活动', 'activ', 0, 2, '/home/activ', '1', 'M', '', '2023-11-05 14:59:43', '2023-11-05 14:59:45', '课程活动目录');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (3, '互动', 'communication', 0, 3, '/home/commu', '1', 'M', '', '2023-11-05 11:09:18', '2023-11-05 11:09:22', '互动聊天目录');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (4, '个人信息管理', 'Avatar', 1, 1, '/home/info/user', 'info/user', 'C', 'info:user:list', '2023-11-05 15:20:51', '2023-11-05 15:20:53', '个人信息管理菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (5, '学生信息管理', 'student', 1, 2, '/home/info/student', 'info/student', 'C', 'info:student:list', '2023-11-05 15:23:35', '2023-11-05 15:23:39', '学生信息管理菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (6, '教师信息管理', 'teacher', 1, 3, '/home/info/teacher', 'info/teacher', 'C', 'info:teacher:list', '2023-11-05 15:23:41', '2023-11-05 15:23:43', '教师信息管理菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (7, '课程列表（学）', 'course', 2, 1, '/home/activ/courseStu', 'activ/courseStu', 'C', 'activ:course:list', '2023-11-05 15:24:40', '2023-11-05 15:24:44', '课程列表菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (8, '课程列表（教）', 'course', 2, 2, '/home/activ/courseTea', 'activ/courseTea', 'C', 'activ:course:list', '2023-11-05 15:24:40', '2023-11-05 15:24:44', '课程列表菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (9, '课程列表（管）', 'course', 2, 3, '/home/activ/courseAdm', 'activ/courseAdm', 'C', 'activ:course:list', '2023-11-05 15:24:40', '2023-11-05 15:24:44', '课程列表菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (10, '提交作业', 'submit', 2, 4, '/home/activ/submit', 'activ/submit', 'C', '', '2023-11-05 15:24:42', '2023-11-05 15:24:46', '提交作业菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (11, '发布作业', 'assign', 2, 5, '/home/activ/assign', 'activ/assign', 'C', '', '2023-11-05 11:45:23', '2023-11-05 11:45:26', '发布作业菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (12, '互评作业', 'grade', 2, 6, '/home/activ/grade', 'activ/grade', 'C', '', '2023-11-05 11:47:05', '2023-11-05 11:47:08', '互评作业菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (13, '成绩', 'score', 2, 7, '/home/activ/score', 'activ/score', 'C', '', '2023-11-05 11:48:35', '2023-11-05 11:48:38', '成绩菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (14, '学生列表', 'stu', 2, 8, '/home/activ/stu', 'activ/stu', 'C', '', '2023-11-05 11:50:46', '2023-11-05 11:50:50', '学生列表菜单');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (15, '讨论区', 'discuss', 3, 1, '/home/commu/discuss', 'commu/discuss', 'C', '', '2023-11-05 11:52:31', '2023-11-05 11:52:34', '讨论区');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (16, '提出申诉', 'complain', 3, 2, '/home/commu/complain', 'commu/complain', 'C', '', '2023-11-05 11:53:44', '2023-11-05 11:53:48', '申诉');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (17, '处理申诉', 'handle', 3, 3, '/home/commu/handle', 'commu/handle', 'C', '', '2023-11-05 11:54:24', '2023-11-05 11:54:27', '处理申诉');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (18, '用户信息修改', '#', 4, 1, '', '1', 'F', 'info:user:edit', '2023-11-12 14:37:22', '2023-11-12 14:37:28', '修改用户信息');
INSERT INTO `sys_menu` (`id`, `name`, `icon`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `perms`, `create_time`, `update_time`, `remark`) VALUES (19, '修改密码', '#', 4, 2, '', '1', 'F', 'info:user:resetPwd', '2023-11-12 15:08:29', '2023-11-12 15:08:33', '重置密码');
