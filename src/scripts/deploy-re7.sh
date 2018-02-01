#!/bin/bash
# Author: Karim (KO4A517N)

set -e

PROJECT_HOME="/home/karim/Workspace/API-OUT/"
ARTIFACT_PATH="/home/karim/Workspace/API-OUT/target/API-OUT-1.0.war"
ARTIFACT_DESTINATION="/opt/tomcat/webapps/"
SERVERS="NOEYYPTZ.adam.adroot.edf.fr:NOEYYPU0.adam.adroot.edf.fr:NOEYYPU1.adam.adroot.edf.fr"
LOGIN="KO4A517N"
PASSWORD="P@ssword10.."
PORT="22"
IFS=":"
SERVERS_AR=($SERVERS)


function build_artifact() {
	f_home=$1

	cd $f_home
	mvn clean install
}

function shutdown_env() {
	f_user=$1
	f_host=$2
	f_pass=$3
	f_path=$4

	/usr/bin/expect << EOD
		spawn /usr/bin/ssh $f_user@$f_host
		expect "password:"
		send "$f_pass\r"
		expect ">"
		send "sudo systemctl stop tomcat\r"
		expect "passe"
		send "$f_pass\r"
		expect ">"
		send "rm -rf $f_path/API*\r"
		expect ">"
		send "bye\r"
EOD

	echo "The Environment is sucessfully configured. Ready to start services !"
}

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

function run_application() {
	f_user=$1
	f_host=$2
	f_pass=$3

	/usr/bin/expect << EOD
		spawn /usr/bin/ssh $f_user@$f_host
		expect "password:"
		send "$f_pass\r"
		expect ">"
		send "sudo systemctl start tomcat\r"
		expect "passe"
		send "$f_pass\r"
		expect ">"
		send "bye\r"
EOD

	echo "The Environment is sucessfully configured. Ready to start services !"
}


# Send Jar to remote servers
for (( i=0; i<${#SERVERS_AR[@]}; i++ ));
do
	#build_artifact $PROJECT_HOME
	shutdown_env $LOGIN ${SERVERS_AR[$i]} $PASSWORD $ARTIFACT_DESTINATION
	#send_ftp $PORT $LOGIN ${SERVERS_AR[$i]} $PASSWORD $ARTIFACT_PATH $ARTIFACT_DESTINATION
	#run_application $LOGIN ${SERVERS_AR[$i]} $PASSWORD
done;

echo "Done !"
