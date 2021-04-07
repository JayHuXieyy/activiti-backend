FROM 172.16.44.49/public/jdk:8-shanghai

COPY government-admin/target/government-admin.jar server.jar

EXPOSE 8000

ENTRYPOINT exec java -jar $JAVA_OPTS server.jar $ENV_PROFILE