#file='/home/dbadmin/log/django-2013-05-07.log.parse_2013-05-09_16-43-07.log-10'

#/opt/vertica/bin/vsql -w bg -c "copy data(device_id,create_date,create_time,ip,user_id,product_id,product_version,product_platform,distribute_oem_id,data_source,device_oem,netword_brand,location,operate,item_id,channel_id,app_id,sns_platform,opt_data) from '" $file +"'  DELIMITER ','"
/opt/vertica/bin/vsql -w bg -c "copy data(device_id,create_date,create_time,ip,user_id,product_id,product_version,product_platform,distribute_oem_id,data_source,device_oem,netword_brand,location,operate,item_id,channel_id,app_id,sns_platform,opt_data) from "$1"  DELIMITER ','"
