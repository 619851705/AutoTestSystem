-----------------------------------------------ϵͳ�û����-----------------------------------------------------------------

--��վϵͳȫ������
create table at_global_setting(
setting_id int auto_increment primary key,
setting_name varchar(255),
default_value varchar(255),
setting_value varchar(255),
mark text
);

INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (1, 'home', 'http://localhost:8080/AutoTest', NULL, '��ҳ��ַurl');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (2, 'notice', '��ӭʹ�����������Զ�������ƽ̨��', NULL, '����');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (3, 'version', '1.0.0', NULL, 'ϵͳ�汾');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (4, 'status', '0', NULL, '��վ״̬-0Ϊ����,1Ϊ�ر�');
INSERT INTO `at_global_setting` (`setting_id`, `setting_name`, `default_value`, `setting_value`, `mark`) VALUES (5, 'logSwitch', '0', NULL, 'log4j����־����');



--��ɫ��Ϣ��
create table at_role(
role_id int auto_increment primary key,
role_group varchar(120),
role_name varchar(120),
mark varchar(255)
);
INSERT INTO `at_role` (`role_id`, `role_group`, `role_name`, `mark`) VALUES (1, '���ܲ�����', 'admin', 'admin');
INSERT INTO `at_role` (`role_id`, `role_group`, `role_name`, `mark`) VALUES (3, '��ͨ�û���', 'default', 'Ĭ����,����ɾ��');


--�û���Ϣ��

create table at_USER(
USER_ID int auto_increment primary key,
USERNAME varchar(120) unique not null,
real_name varchar(120) not null,
PASSWORD varchar(120) not null,
ROLE_ID int,
CREATE_TIME datetime,
status char(1),
last_login_time datetime,
if_new char(1)
);
alter table at_USER add constraint at_user_fk_role_id foreign key(role_id) REFERENCES at_role(role_id);
INSERT INTO `at_user` (`USER_ID`, `USERNAME`, `real_name`, `PASSWORD`, `ROLE_ID`, `CREATE_TIME`, `status`, `last_login_time`, `if_new`) VALUES (1, 'xuwangcheng', '������', '1111', 1, '2016-09-03 19:07:47', '0', '2016-09-26 12:01:24', '1');
a

--�û��ʼ���Ϣ��
create table at_user_mail(
mail_id int auto_increment primary key,
receive_user_id int,
send_user_id int,
if_validate char(1),
mail_info longtext,
send_status char(1),
read_status char(1),
send_time datetime
);
alter table at_user_mail add constraint at_user_mail_fk_user_id_receive foreign key(receive_user_id) references at_user(user_id);
alter table at_user_mail add constraint at_user_mail_fk_user_id_send foreign key(send_user_id) references at_user(user_id);
	

--�ӿڲ�����Ϣ��
create table at_operation_interface(
op_id int auto_increment primary key,
op_name varchar(255),
call_name varchar(255),
is_parent char(20),
mark varchar(255),
status char(1),
parent_op_id int
);	
alter table at_operation_interface add constraint at_operation_interface_fk_parent_op_id foreign key(parent_op_id) references at_operation_interface(op_id);		

--��ɫ����Ȩ�ޱ�
create table at_role_power(
role_id int,
op_id int,
PRIMARY KEY  (role_id,op_id)
);		

alter table at_role_power add constraint at_role_power_fk_role_id foreign key(role_id) references at_role(role_id);
alter table at_role_power add constraint at_role_power_fk_op_id foreign key(op_id) references at_operation_interface(op_id) ON DELETE CASCADE;

--��ѯ�����õ����ݿ���������
create table at_data_db(
db_id int primary key,
db_type varchar(10),
db_url varchar(255),
db_name varchar(255),
db_username varchar(120),
db_passwd varchar(120),
db_mark varchar(255)
);


--�Զ������Զ�ʱ�����
CREATE TABLE `at_task` (
	`task_id` INT(11) AUTO_INCREMENT,
	`task_name` varchar(255),
	`task_type` CHAR(1),
	`related_id` INT(11),
	`task_cron_expression` VARCHAR(100) ,
	`run_count` INT(11) NULL DEFAULT 0,
	`last_finish_time` DATETIME,
	`create_time` DATETIME,
	`status` CHAR(1),
	PRIMARY KEY (`task_id`)
);
-----------------------------------�ӿ��Զ�������------------------------------------------
--�ӿ���Ϣ��
create table at_interface_info(
interface_Id int auto_increment primary key,
interface_name varchar(120) unique not null,
interface_cn_name varchar(120),
request_url_mock text,
request_url_real text,
interface_type char(2),
create_time datetime,
status char(1),
user_id int,
last_modify_user varchar(120)
);
alter table at_interface_info add constraint at_interface_fk_user_id foreign key(user_id) REFERENCES at_user(user_id);

--������Ϣ��
create table at_Parameter(
parameter_id int auto_increment primary key,
parameter_identify varchar(120) not null,
parameter_name varchar(120),
default_value varchar(256),
type varchar(50),
interface_id int
);
alter table at_Parameter add constraint at_Parameter_fk_interface_id foreign key(interface_id) REFERENCES at_interface_info(interface_id);

--������Ϣ��
create table at_message(
message_id int auto_increment primary key,
message_name varchar(255),
interface_id int,
parameter_id int,
parameter_json longtext,
request_url text,
user_id int,
create_time datetime,
status char(1),
last_Modify_User varchar(120)
);
alter table at_message add constraint at_message_fk_interface_id foreign key(interface_id) references at_interface_info(interface_id);
alter table at_message add constraint at_message_fk_parameter_id foreign key(parameter_id) references at_parameter(parameter_id);
alter table at_message add constraint at_message_fk_user_id foreign key(user_id) references at_user(user_id);


--���Ӳ�����ɹ����
create table at_Complex_Parameter(
id int auto_increment primary key,
self_parameter_id int,
next_parameter_id int
);
alter table at_Complex_Parameter add constraint at_Complex_Parameter_fk_self_parameter_Id foreign key(self_parameter_Id) references at_parameter(parameter_id);
alter table at_Complex_Parameter add constraint at_Complex_Parameter_fk_next_parameter_id foreign key(next_parameter_id) references at_parameter(parameter_id);

--���ĳ�����
create table at_message_scene(
message_scene_id int auto_increment primary key,
message_id int,
scene_name varchar(255),
validate_rule_flag char(1),
mark TEXT
);
alter table at_message_scene add constraint at_message_scene_pk_message_id foreign key(message_id) references at_message(message_id);

--���ĳ��������֤����
create table at_scene_validate_rule(
validate_id int auto_increment primary key,
message_scene_id int,
parameter_name varchar(255),
validate_value text,
get_value_method VARCHAR(20),
full_validate_flag char(1),
complex_flag char(1),
status char(1),
mark text
);

alter table at_scene_validate_rule add constraint at_scene_validate_rule_fk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id);

--������Ӧ�Ĳ�������
create table at_test_data(
data_id int auto_increment primary key,
message_scene_id int,
params_data longtext,
status char(1)
);
alter table at_test_data add  data_discr varchar(256);
alter table at_test_data add constraint at_test_data_pk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id);

--���Լ�
create table at_test_set(
set_id int auto_increment primary key,
set_name varchar(255),
user_id int,
create_time datetime,
status char(1),
mark varchar(255)
);
alter table at_test_set add constraint at_test_set_fk_user_id foreign key(user_id) references at_user(user_id);

--���Լ����Գ�������
create table at_set_scene(
set_id int,
message_scene_id int,
PRIMARY KEY  (set_id,message_scene_id)
);
alter table at_set_scene add constraint at_set_scene_fk_set_id foreign key(set_id) references at_test_set(set_id);
alter table at_set_scene add constraint at_set_scene_fk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id) ON DELETE CASCADE;

--���Ա����
create table at_test_report(
report_id int auto_increment primary key,
test_mode varchar(10),
finish_flag char(1),
start_time datetime,
finish_time datetime,
user_id int
);
alter table at_test_report add constraint at_test_report_fk_user_id foreign key(user_id) references at_user(user_id);

--���Խ�������
create table at_test_result(
result_id int auto_increment primary key,
message_scene_id int,
report_id int,
message_info varchar(255),
request_url varchar(255),
request_message longtext,
response_message longtext,
use_time int,
run_status char(1),
status_code varchar(10),
op_time datetime,
mark longtext
);
alter table at_test_result add constraint at_test_result_fk_message_scene_id foreign key(message_scene_id) references at_message_scene(message_scene_id);
alter table at_test_result add constraint at_test_result_fk_report_id foreign key(report_id) references at_test_report(report_id);

--���Խ��ȱ�
create table at_test_process(
process_id int auto_increment primary key,
report_id int,
current_process_percent int,
current_info varchar(255),
complete_tag char(1)
);

--�������ñ�
create table at_test_config(
config_id int auto_increment primary key,
user_id int,
request_Url_Flag char(1),
connect_Time_Out int,
read_Time_Out int,
http_Method_Flag char(5),
validate_String varchar(255),
check_Data_flag char(1),
background_Exec_flag char(1)
);

insert into at_test_config values(null,0,'0',10000,5000,'0','0,0000,000000','0','1');

--�ӿ�mock��Ϣ
create table at_interface_mock(
mock_id int auto_increment primary key,
interface_name varchar(100) unique not null,
mock_url varchar(255),
request_json longtext,
response_json longtext,
create_time datetime,
user_id int,
if_validate char(1),
call_count int,
status char(1)
);

alter table at_interface_mock add constraint at_interface_mock_fk_user_id foreign key(user_id) references at_user(user_id);


----web�Զ���������ر�
--���Զ������
create table at_web_object_category(
category_id int auto_increment primary key,
category_name varchar(120),
category_type varchar(50),
parent_category_id int
);

alter table at_web_object_category add constraint at_web_object_category_parent_category_id foreign key(parent_category_id) references at_web_object_category(category_id);
INSERT INTO `at_web_object_category` (`category_id`, `category_name`, `category_type`, `parent_category_id`) VALUES (1, 'Web�Զ�������', 'all', NULL);

--���Զ���
create table at_web_object(
object_id int auto_increment primary key,
object_name varchar(120),
object_type varchar(100),
how varchar(100),
object_seq int,
property_value varchar(255),
page_url text,
category_id int
);

alter table at_web_object add constraint at_web_object_category_id foreign key(category_id) references at_web_object_category(category_id);


--����������
create table at_web_case(
case_id int auto_increment primary key,
case_name varchar(120),
case_desc text,
browser varchar(50),
run_flag char(1),
user_id int,
create_time datetime
);
alter table at_web_case add constraint at_web_case_user_id foreign key(user_id) references at_user(user_id);

--�����������Լ��ϱ�
create table at_web_case_set(
set_id int auto_increment  primary key,
set_name varchar(100),
set_desc text,
test_count int,
user_id int,
create_time datetime,
last_modify_user varchar(100),
status char(1)
);
alter table at_web_case_set add constraint at_web_case_set_fk_user_id foreign key(user_id) references at_user(user_id);

--�����������������Ϲ�����
create table at_web_case_set_comp(
id int auto_increment primary key,
set_id int,
case_id int,
status char(1),
user_id int,
submit_time datetime);
alter table at_web_case_set_comp add constraint at_web_case_set_comp_fk_set_id foreign key(set_id) references at_web_case_set(set_id);
alter table at_web_case_set_comp add constraint at_web_case_set_comp_fk_case_id foreign key(case_id) references at_web_case(case_id);
alter table at_web_case_set_comp add constraint at_web_case_set_comp_fk_user_id foreign key(user_id) references at_user(user_id);

--���Բ����
create table at_web_step(
step_id int auto_increment primary key,
order_num int,
case_id int,
step_desc text,
step_method varchar(50),
object_id int,
call_method varchar(50),
require_parameter text,
require_value text,
require_parameter_type char(10),
capture char(1)
);
alter table at_web_step add constraint at_web_step_case_id foreign key(case_id) references at_web_case(case_id);
alter table at_web_step add constraint at_web_step_object_id foreign key(object_id) references at_web_object(object_id);

--���Ա����
create table at_web_report_set(
report_set_id int auto_increment primary key,
set_id int,
test_time datetime
);
alter table at_web_report_set add constraint at_web_report_set_set_id foreign key(set_id) references at_web_case_set(set_id);

create table at_web_report_case(
report_case_id int auto_increment primary key,
case_id int,
report_set_id int,
test_time datetime
);
alter table at_web_report_case add constraint at_web_report_set_case_id foreign key(case_id) references at_web_case(case_id);
alter table at_web_report_case add constraint at_web_report_case_report_set_id foreign key(report_set_id) references at_web_report_set(report_set_id);

create table at_web_report(
report_id int auto_increment primary key,
step_id int,
report_case_id int,
run_status varchar(20),
test_mark text,
capture_path varchar(255),
test_user_name varchar(100),
op_time datetime
);
alter table at_web_report add constraint at_web_report_step_id foreign key(step_id) references at_web_step(step_id);
alter table at_web_report add constraint at_web_report_report_case_id foreign key(report_case_id) references at_web_report_case(report_case_id);

--web�Զ����û��������ñ�
create table at_web_config(
config_id int auto_increment primary key,
user_id int,
element_wait_time int,
result_wait_time int,
ie_path varchar(255),
chrome_path varchar(255),
firefox_path varchar(255),
opera_path varchar(255),
window_size char(1),
error_interrupt_flag char(1)
);
INSERT INTO `at_web_config` (`config_id`, `user_id`, `element_wait_time`, `result_wait_time`, `ie_path`, `chrome_path`, `firefox_path`, `opera_path`, `window_size`, `error_interrupt_flag`) VALUES (1, 0, 5000, 3000, '', '', '', '', '1', '0');

--web�Զ����������Բ�������
create table at_web_step_category(
category_id int auto_increment primary key,
category_name varchar(255),
category_desc text,
create_user varchar(100),
submit_time datetime,
handle_time datetime,
category_tag varchar(255),
use_count int,
status char(1)
);		



--web�Զ����������Բ����
create table at_web_public_step(
step_id int auto_increment primary key,
order_num int,
step_desc varchar(255),
step_method varchar(50),
object_id int,
call_method varchar(50),
require_parameter text,
require_value text,
require_parameter_type char(10),
capture char(1),
category_id int
);	
alter table at_web_public_step add constraint at_web_public_step_object_id foreign key(object_id) references at_web_object(object_id);
alter table at_web_public_step add constraint at_web_public_step_category_id foreign key(category_id)	references at_web_step_category(category_id);

--web�Զ���Զ�̲��Լ�¼��
create table at_web_test_rmi(
test_id int auto_increment primary key,
user_id int,
test_mode char(1),
related_id int,
submit_time datetime,
finish_time datetime,
status char(1),
test_msg text,
task_name varchar(255)
);
alter table at_web_test_rmi add constraint at_web_test_rmi_user_id foreign key(user_id) references at_user(user_id);
--------------------------------------------------------ruby�ű�------------------------------------------------------------------
--ruby�ű���Ϣ��
create table at_web_script_info(
script_id int auto_increment primary key,
script_name varchar(255),
script_path varchar(255),
if_public char(1),
script_desc text,
script_author varchar(80),
create_time datetime,
last_run_time datetime
);

--ruby�ű����в��Ա���
create table at_web_script_report(
report_id int auto_increment primary key,
report_name varchar(255),
report_path varchar(255),
case_num int,
user_id int,
test_time datetime,
test_mark text
);

alter table at_web_script_report add constraint at_web_script_report_fk_user_id foreign key(user_id) references at_user(user_id);


-------------------------------------------------------�����ӿ��б�----------------------------------------------------------
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (1, '�Զ�������ƽ̨', '', 'true', 'ȫ�ָ��ڵ�', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (2, '�ӿ��Զ���', NULL, 'true', '�ӿ��Զ���', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (3, '�ӿڹ���', 'interface', 'true', '�ӿڹ���ڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (4, '��ȡ���нӿ��б�', 'interface-list', 'false', '�鿴��ǰ��������״̬�����нӿ�', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (5, 'ɾ��ָ���ӿ�', 'interface-del', 'false', 'ɾ��ָ��id�Ľӿ���Ϣ,ͬ����ɾ����������ı��ĺͳ���������', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (6, '���ӽӿ���Ϣ', 'interface-add', 'false', '����һ���µĽӿ���Ϣ', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (7, '�鿴ָ���ӿڵ����', 'interface-showParameters', 'false', '�鿴ָ���ӿڵ���������б�', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (8, 'ɾ���ӿڵ�һ������', 'interface-delParameter', 'false', 'ɾ��ĳ���ӿڵ�һ������', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (9, '�༭�ӿڲ���', 'interface-editParameter', 'false', '�༭ָ���ӿڵ�ָ��������Ϣ', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (10, '����һ���ӿڲ���', 'interface-saveParameter', 'false', '��ָ���ӿ�����һ������', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (11, 'ͨ��json����ָ���ӿڵĲ�����Ϣ', 'interface-batchImportParams', 'false', 'ͨ��json�ַ�����������ӿڵĲ���', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (12, '��ѯָ���ӿ���Ϣ', 'interface-find', 'false', 'ͨ��id����ָ���ӿڵ���Ϣ', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (13, '���½ӿ���Ϣ', 'interface-update', 'false', '�༭����ָ���ӿڵ���ϸ��Ϣ', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (14, '�����������ҽӿ�', 'interface-filter', 'false', '���ݴ���Ĳ�ѯ��Ian���ҽӿ�', '0', 3);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (15, '�Զ�������', 'test', 'true', 'autoTes�ӿ��Զ������Խڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (16, '����������', 'test-sceneTest', 'false', '���Ե������ĳ���', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (17, '��ȡ��������', 'test-getConfig', 'false', '��ȡָ���û��Ĳ�������', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (18, '����������Ϣ', 'test-updateConfig', 'false', '����ָ���û���������Ϣ', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (19, '�����Լ�����', 'test-validateData', 'false', '��ʼ���Լ�����֮ǰ�������', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (20, '��ִ̨�в���', 'test-backgroundTest', 'false', '��ִ̨�в��Լ�����', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (21, '�Ǻ�ִ̨�в���', 'test-commonTest', 'false', '�Ǻ�ִ̨�в��Լ�����', '0', 15);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (22, '���Ĺ���', 'message', 'true', '���Ĺ���ڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (23, '��ȡ�����б�', 'message-list', 'false', '��ȡ�����б�', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (24, 'ɾ������', 'message-del', 'false', 'ɾ��ָ������', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (25, '��ʽ��json��', 'message-format', 'false', '��ʽ������json��', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (26, '��֤������κϷ���', 'message-validateJson', 'false', '��֤������κϷ���', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (27, '�����±���', 'message-add', 'false', '�����±���', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (28, '��ȡ�������json��', 'message-getParamsJson', 'false', '��ȡ�������json��', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (29, '��ȡָ��������Ϣ', 'message-get', 'false', '��ȡָ��������Ϣ', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (30, '����ָ��������Ϣ', 'message-edit', 'false', '����ָ��������Ϣ', '0', 22);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (31, '��������', 'messageScene', 'true', '��������ڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (32, '��ȡ�����б�', 'messageScene-list', 'false', '��ȡ���ĳ����б�', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (33, '��������', 'messageScene-save', 'false', '�����µı��ĳ���', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (34, 'ɾ������', 'messageScene-del', 'false', 'ɾ��ָ������', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (35, '���³�����Ϣ', 'messageScene-edit', 'false', '�༭���³�����Ϣ', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (36, '��ȡָ��������Ϣ', 'messageScene-get', 'false', '��ȡָ��������Ϣ', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (37, 'ɾ��ָ����������', 'messageScene-delData', 'false', 'ɾ��ָ����������', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (38, '��ȡָ������������Ϣ', 'messageScene-getData', 'false', '��ȡָ������������Ϣ', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (39, '����ָ������������Ϣ', 'messageScene-updateDataJson', 'false', '����ָ������������Ϣ', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (40, '�����µĲ�������', 'messageScene-saveData', 'false', '�����µĲ�������', '0', 31);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (41, '�ӿ�mock����', 'mock', 'true', '�ӿ�mock����ڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (42, '��ȡ�ӿ�mock�б�', 'mock-list', 'false', '��ȡ�ӿ�mock�б�', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (43, '��ȡָ���ӿ�mock��Ϣ', 'mock-get', 'false', '��ȡָ���ӿ�mock��Ϣ', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (44, 'ɾ��ָ��mock�ӿ�', 'mock-del', 'false', 'ɾ��ָ��mock�ӿ�', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (45, '�༭�����ӽӿ�mock��Ϣ', 'mock-edit', 'false', '�༭�����ӽӿ�mock��Ϣ', '0', 41);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (46, '���Ա������', 'report', 'true', '���Ա������ڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (47, '���Լ�����', 'set', 'true', '���Լ�����ڵ�', '0', 2);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (48, '��ȡ���Ա����б�', 'report-list', 'false', '��ȡ���Ա����б�', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (49, '�鿴���������б�', 'report-showResult', 'false', '�鿴���������б�', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (50, '�鿴ָ�������Ĳ�������', 'report-showResultDetail', 'false', '�鿴ָ�������Ĳ�������', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (51, '������������', 'report-htmlView', 'false', '������������', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (52, 'ɾ�����Ա���', 'report-del', 'false', 'ɾ�����Ա���', '0', 46);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (53, '��ȡ���Լ��б�', 'set-list', 'false', '��ȡ���Լ��б�', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (54, 'ɾ�����Լ�', 'set-del', 'false', 'ɾ�����Լ�', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (55, '�������Լ�', 'set-save', 'false', '�������Լ�', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (56, '��ȡָ�����Լ���Ϣ', 'set-find', 'false', '��ȡָ�����Լ���Ϣ', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (57, '���²��Լ���Ϣ', 'set-edit', 'false', '���²��Լ���Ϣ', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (58, '��ȡ���Լ��µĲ��Գ���', 'set-getScenes', 'false', '��ȡ���Լ��µĲ��Գ���', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (59, 'ɾ�����Լ��µĳ���', 'set-delScene', 'false', 'ɾ�����Լ��µĳ���', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (60, '��ȡ���Լ�������ӵĲ��Գ���', 'set-getNotScenes', 'false', '��ȡ���Լ�������ӵĲ��Գ���', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (61, '���Լ����ӳ���', 'set-addScene', 'false', '���Լ����ӳ���', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (62, '��ȡ��ǰ�û��Ĳ��Լ��б�', 'set-getMy', 'false', '��ȡ��ǰ�û��Ĳ��Լ��б�', '0', 47);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (63, 'ϵͳ����', NULL, 'true', 'ϵͳ����ڵ�', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (64, '��ѯ���ݹ���', 'db', 'true', '��ѯ���ݿ����ڵ�', '0', 63);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (65, '��ȡ��ѯ���ݿ���Ϣ�б�', 'db-list', 'false', '��ȡ��ѯ���ݿ���Ϣ�б�', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (66, 'ɾ����ѯ���ݿ���Ϣ', 'db-del', 'false', 'ɾ����ѯ���ݿ���Ϣ', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (67, '���²�ѯ���ݿ���Ϣ', 'db-edit', 'false', '���²�ѯ���ݿ���Ϣ', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (68, '��ȡָ�����ݿ���Ϣ', 'db-get', 'false', '��ȡָ�����ݿ���Ϣ', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (69, '�������ݿ�����', 'db-testDB', 'false', '�������ݿ�����', '0', 64);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (70, '�û���ɫ����', NULL, 'true', '�û���ɫȨ�޹���', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (71, '��ɫ����', 'role', 'true', '��ɫ����ڵ�', '0', 70);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (72, '�û�����', 'user', 'true', '�û�����ڵ�', '0', 70);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (73, '��ȡ��ɫ�б�', 'role-list', 'false', '��ȡ��ɫ�б�', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (74, 'ɾ��ָ����ɫ', 'role-del', 'false', 'ɾ��ָ����ɫ', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (75, '��ȡָ����ɫ��Ϣ', 'role-get', 'false', '��ȡָ����ɫ��Ϣ', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (76, '���½�ɫ��Ϣ', 'role-edit', 'false', '���½�ɫ��Ϣ', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (77, '��ȡ��ɫȨ����Ϣ', 'role-getNodes', 'false', '��ȡ��ɫȨ����Ϣ', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (78, '���½�ɫȨ����Ϣ', 'role-updateRolePower', 'false', '���½�ɫȨ����Ϣ', '0', 71);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (79, '��ȡ��ǰ�û��б�', 'user-list', 'false', '��ȡ��ǰ�û��б�', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (80, 'ɾ��ָ���û�', 'user-del', 'false', 'ɾ��ָ���û�', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (81, '���������ָ���û�', 'user-lock', 'false', '���������ָ���û�', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (82, '�����û�����', 'user-resetPwd', 'false', '�����û�����', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (83, '��ȡָ���û���Ϣ', 'user-get', 'false', '��ȡָ���û���Ϣ', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (84, '�����û���Ϣ', 'user-edit', 'false', '�����û���Ϣ', '0', 72);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (85, 'web�Զ�������', NULL, 'true', 'web�Զ������Խڵ�', '0', 1);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (86, '������������', 'webCase', 'true', '������������ڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (87, '��������������', 'caseSet', 'true', '��������������ڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (88, '�������ù���', 'webConfig', 'true', '�������ù���ڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (89, '���Զ������', 'webObject', 'true', '���Զ������ڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (90, '���Ա������', 'webReport', 'true', '���Ա������ڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (91, '���Թ�����������', 'publicStep', 'true', '���Թ�����������ڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (92, 'web�Զ�������', 'webTest', 'true', '�Զ������Խڵ�', '0', 85);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (93, '��ȡ���������б�', 'webCase-list', 'false', '��ȡ���������б�', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (94, '���в�������(�����)', 'webCase-runTest', 'false', '���в�������(�����)', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (95, 'ɾ��ָ����������', 'webCase-delCase', 'false', 'ɾ��ָ����������', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (96, '�༭���²�������������Ϣ', 'webCase-editCase', 'false', '�༭���²���������Ϣ', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (97, '��ȡָ������������Ϣ', 'webCase-getCase', 'false', '��ȡָ������������Ϣ', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (98, '��ȡָ�����Բ�����Ϣ', 'webCase-getStep', 'false', '��ȡָ�����Բ�����Ϣ', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (99, '�༭ָ�����Բ���', 'webCase-editStep', 'false', '�༭ָ�����Բ���', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (100, '��ȡָ�����������µĲ��Բ���', 'webCase-listStep', 'false', '��ȡָ�����������µĲ��Բ���', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (101, 'ɾ��ָ�����Բ���', 'webCase-delStep', 'false', 'ɾ��ָ�����Բ���', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (102, '���Բ�������', 'webCase-sortSteps', 'false', '���Բ�������', '0', 86);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (103, '��ȡ�����������б�', 'caseSet-list', 'false', '��ȡ�����������б�', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (104, '���Ӳ������������Լ�', 'caseSet-addToSet', 'false', '���Ӳ������������Լ�', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (105, 'ɾ�����Լ�', 'caseSet-delSet', 'false', 'ɾ�����Լ�', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (106, '�༭���²��Լ���Ϣ', 'caseSet-editSet', 'false', '�༭���²��Լ���Ϣ', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (107, '��ȡ��ǰ�û�����˼�¼', 'caseSet-auditRecord', 'false', '��ȡ��ǰ�û�����˼�¼', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (108, '���Լ�����(��������)', 'caseSet-batchTest', 'false', '���Լ�����(��������)', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (109, '���²�����������Լ��Ĺ���״̬(��˻��ߴ��)', 'caseSet-updateCompStatus', 'false', '���²�����������Լ��Ĺ���״̬(��˻��ߴ��)', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (110, '��ȡָ��������������Ϣ', 'caseSet-getSet', 'false', '��ȡָ��������������Ϣ', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (111, '��ȡ���Լ��µĲ��������б�', 'caseSet-getSetCase', 'false', '��ȡ���Լ��µĲ��������б�', '0', 87);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (112, '��ȡ��ǰ�û��������ñ�', 'webConfig-get', 'false', '��ȡ��ǰ�û��������ñ�', '0', 88);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (113, '�༭��ǰ�û��������ñ�', 'webConfig-edit', 'false', '�༭��ǰ�û��������ñ�', '0', 88);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (114, '���Ӳ��Զ�����ڵ�', 'webObject-addCategory', 'false', '���Ӳ��Զ�����ڵ�', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (115, '�޸Ĳ��Զ���ڵ��ϵ', 'webObject-moveCategory', 'false', '�޸Ĳ��Զ���ڵ��ϵ', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (116, '��ȡָ���ڵ��µĲ��Զ������ȫ������', 'webObject-list', 'false', '��ȡָ���ڵ��µĲ��Զ������ȫ������', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (117, '���ӻ����޸Ĳ��Զ���', 'webObject-edit', 'false', '���ӻ����޸Ĳ��Զ���', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (118, '��ȡָ�����Զ�����Ϣ', 'webObject-get', 'false', '��ȡָ�����Զ�����Ϣ', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (119, 'ɾ��ָ�����Զ���', 'webObject-del', 'false', 'ɾ��ָ�����Զ���', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (120, '��ȡ���в��Զ���ڵ���Ϣ', 'webObject-getNodes', 'false', '��ȡ���в��Զ���ڵ���Ϣ', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (121, 'ɾ��ָ���ڵ�', 'webObject-delCategory', 'false', 'ɾ��ָ���ڵ�', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (122, '�޸Ľڵ�����', 'webObject-updateCategoryName', 'false', '�޸Ľڵ�����', '0', 89);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (123, '��ȡ���Լ������б�', 'webReport-listReportSet', 'false', '��ȡ���Լ������б�', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (124, '��ȡָ�����Լ�������Ϣ', 'webReport-getReportSet', 'false', '��ȡָ�����Լ�������Ϣ', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (125, 'ɾ��ָ�����Լ�����', 'webReport-delReportSet', 'false', 'ɾ��ָ�����Լ�����', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (126, '��ȡ�������������б�', 'webReport-listReportCase', 'false', '��ȡ�������������б�', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (127, 'ɾ��ָ��������������', 'webReport-delReportCase', 'false', 'ɾ��ָ��������������', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (128, '��ȡ�������������µĲ��豨���б�', 'webReport-stepReport', 'false', '��ȡ�������������µĲ��豨���б�', '0', 90);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (129, '��ȡ���������б�', 'publicStep-listCategory', 'false', '��ȡ���������б�', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (130, '����ָ���������赽��������', 'publicStep-copySteps', 'false', '����ָ���������赽��������', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (131, '�Ƽ��Լ��Ĳ��Բ��赽���������', 'publicStep-addCategory', 'false', '�Ƽ��Լ��Ĳ��Բ��赽���������', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (132, '��ȡ��ǰ�û����Ƽ���ʷ', 'publicStep-auditRecord', 'false', '��ȡ��ǰ�û����Ƽ���ʷ', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (133, '����û��Ƽ�', 'publicStep-updateCategoryStatus', 'false', '����û��Ƽ�(����category״̬)', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (134, '��ȡָ��������µĲ����б�', 'publicStep-listStep', 'false', '��ȡָ��������µĲ����б�', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (135, '��ȡָ���������Բ���', 'publicStep-getStep', 'false', '��ȡָ���������Բ���', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (136, '�༭ָ���������Բ���', 'publicStep-editStep', 'false', '�༭ָ���������Բ���', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (137, 'ɾ��ָ�����������', 'publicStep-delStepCategory', 'false', 'ɾ��ָ�����Թ��������', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (138, '���¹�������ⱸע��Ϣ', 'publicStep-editCategory', 'false', '���¹�������ⱸע��Ϣ', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (139, '��ȡ��ǰ��������������б�', 'publicStep-categoryAudit', 'false', '��ȡ��ǰ��������������б�', '0', 91);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (140, '��ȡ��ǰ���������б�(�ͻ���)', 'webTest-taskList', 'false', '��ȡ��ǰ���������б�(�ͻ���)', '0', 92);
INSERT INTO `at_operation_interface` (`op_id`, `op_name`, `call_name`, `is_parent`, `mark`, `status`, `parent_op_id`) VALUES (141, '���Ͳ������󵽿ͻ���(�����µĿͻ��˲�������)', 'webTest-runTest', 'false', '���Ͳ������󵽿ͻ���(�����µĿͻ��˲�������)', '0', 92);

