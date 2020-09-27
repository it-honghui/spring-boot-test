# build stage
FROM registry.cn-beijing.aliyuncs.com/gathub/maven:3.6-pro as build-stage
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# production stage
FROM registry.cn-beijing.aliyuncs.com/gathub/java:8-jre as production-stage
WORKDIR /code/
COPY --from=build-stage /app/target/app.jar .
RUN ln -fs /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && dpkg-reconfigure -f noninteractive tzdata
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Xms300m -Xmx300m -jar app.jar --spring.profiles.active=prod