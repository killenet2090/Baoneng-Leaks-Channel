use bnmotor_icv_vehicle_fota;

-- ������ڣ�2021-03-05
-- ����ˣ���С��



-- �޸İ�װ����״̬˵��
ALTER TABLE tb_fota_version_check_verify
MODIFY COLUMN `installed_complete_status` smallint(10) DEFAULT NULL COMMENT '��װ�����1=������ɣ��ɹ�����2=����δ��ɣ�3=����ʧ��,4=������ֹ���жϣ�';