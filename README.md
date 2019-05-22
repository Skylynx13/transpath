# transpath
transpath
Using for contents management.

# set env as:
#!/bin/bash
export JAVA_HOME="/c/app/jdk1.8.0_131"
export JDK_HOME=${JAVA_HOME}
export M2_HOME="/c/app/apache-maven-3.5.0"
export M2=${M2_HOME}/bin
export PATH=${JAVA_HOME}/bin:${M2}:${PATH}

# install lib
mvn install:install-file -DgroupId=org.apache.commons.codec -DartifactId=commons-codec -Dversion=1.10 -Dpackaging=jar -Dfile=commons-codec-1.10.jar

# use ssh-add to get github permission
ssh-agent bash
ssh-add ~/.ssh/skylynx13_rsa

# rar and unzip support
English version of rar should be installed and system path be set.

