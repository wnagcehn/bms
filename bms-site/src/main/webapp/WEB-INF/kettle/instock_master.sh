
#date1=$(date +%Y%m%d)
#/wubangjun/pdi-ce-5.4.0.1-130/data-integratio/kitchen.sh -file=/wubangjun/BmsKettle/BmsInstockMasterJob.kjb -logfile=/wubangjun/BmsKettle/log/instockMaster.$date1.log

##########################################################################################################################

export JAVA_HOME=/usr/java/jdk1.7.0_79 
export PATH=$JAVA_HOME/bin:$PATH 
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export KETTLE_HOME=/usr/local/kettle/data-integration
cd /usr/local/kettle/data-integration
date1=$(date +%Y%m%d)
./pan.sh -file=/usr/local/kettle/BmsKettle/instock_master.ktr -logfile=/usr/local/kettle/BmsKettle/logs/instock_master.$date1.log

##########################################################################################################################
 
