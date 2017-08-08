#!/bin/bash
IFS='
'
# 日志目录
log_path=('/d20161111/log/quartz' '/d20161111/log/tomcat_front/logs' '/d20161111/log/tomcat_msite/logs' '/d20161111/log/tomcat_back/logs' '/d20161111/log/tomcat_app_front/logs')
# 昨天
yesterday=`date +"%Y%m%d" -d "-1day"`

idx=0
for p in ${log_path[@]};
do
	# 文件日期
	log_p_date=(`ls -l "$p" --time-style '+%Y%m%d' | awk '{print$6}'`)
	# 文件名
	log_p_file=(`ls -l "$p" --time-style '+%Y%m%d' | awk '{print$7}'`)
	target_path=${p/'d20161111'/'mnt'}
	#echo $target_path
	log_idx=0
	for d in ${log_p_date[@]};
	do
        	if [ $d -le $yesterday ]; then
                	#echo $target_path"/"${log_p_file[$log_idx]}
			mv -f  ${p}"/"${log_p_file[$log_idx]} ${target_path}"/"${log_p_file[$log_idx]}			
        	fi
		let log_idx+=1
	done
	let idx+=1	
done

:<<!
index=0
for i in ${fdarr[@]};
do
	fdate=`date -d "$i" +%s`
	if [ $i -lt $yesterday ]; then
		echo "if > " $yesterday ${fnarr[$index]}
	else
		echo "else > " $i
	fi
	let index+=1
done
!
