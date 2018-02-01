#!/bin/bash
# Author: Karim (KO4A517N)

set -e

function send_ftp() {
	f_port=$1
	f_user=$2
	f_host=$3
	f_pass=$4
	f_file=$5
	f_targ=$6

	/usr/bin/expect << EOD
		spawn /usr/bin/sftp -o StrictHostkeyChecking=no -o UserKnownHostsFile=/dev/null -o Port=$f_port $f_user@$f_host
		expect "password:"
		send "$f_pass\r"
		expect "sftp>"
		send "put $f_file $f_targ\r"
		expect "sftp>"
		send "bye\r"
EOD

	echo "The following File was sent sucessfully : $f_file"
}

function setup_env() {
	f_user=$1
	f_host=$2
	f_pass=$3
	f_path=$4

	/usr/bin/expect << EOD
		spawn /usr/bin/ssh $f_user@$f_host
		expect "password:"
		send "$f_pass\r"
		expect ">"
		send "mkdir -p $f_path\r"
		expect ">"
		send "pkill -u '$f_user' -f 'java'\r"
		expect ">"
		send "bye\r"
EOD

	echo "The Environment is sucessfully configured. Ready to start services !"
}

function run_application() {
	f_user=$1
	f_host=$2
	f_pass=$3
	f_path=$4
	f_name=$5

	/usr/bin/expect << EOD
		spawn /usr/bin/ssh $f_user@$f_host
		expect "password:"
		send "$f_pass\r"
		expect ">"
		send "java -jar $f_name -server -Xms1g -Xmx3g -XX:+UseG1GC -XX:MaxGCPauseMillis=500 -XX:InitiatingHeapOccupancyPercent=50 -XX:+UseCompressedOops -XX:MaxTenuringThreshold=10 -XX:ParallelGCThreads=8 -XX:ConcGCThreads=8 -XX:G1ReservePercent=10 -XX:G1HeapRegionSize=32m\r"
		send "bye\r"
EOD

	echo "The Environment is sucessfully configured. Ready to start services !"
}

JAR_NAME="API-OUT-1.0.jar"
PROJECT_HOME="/home/karim/Workspace/API-OUT/"
ARTIFACT_PATH="/home/karim/Workspace/API-OUT/target/API-OUT-1.0.jar"
TODAY=$(date +"%Y%m%d-%H%M")
ARTIFACT_DESTINATION="/home/KO4A517N/API-OUT/$TODAY/"
SERVERS="NOEYYPTZ.adam.adroot.edf.fr:NOEYYPU0.adam.adroot.edf.fr:NOEYYPU1.adam.adroot.edf.fr"
LOGIN="KO4A517N"
PASSWORD="P@ssword10.."
PORT="22"
IFS=":"
SERVERS_AR=($SERVERS)

# Maven build project
cd $PROJECT_HOME
mvn clean install

# Send Jar to remote servers
for (( i=0; i<${#SERVERS_AR[@]}; i++ ));
do
	setup_env $LOGIN ${SERVERS_AR[$i]} $PASSWORD $ARTIFACT_DESTINATION
	send_ftp $PORT $LOGIN ${SERVERS_AR[$i]} $PASSWORD $ARTIFACT_PATH $ARTIFACT_DESTINATION
	run_application $LOGIN ${SERVERS_AR[$i]} $PASSWORD $ARTIFACT_DESTINATION $JAR_NAME
done;

echo "Done !"




