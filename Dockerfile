FROM openjdk:21-jdk

# Criação de um usuário não-root para segurança
RUN groupadd --system spring && useradd --system --create-home --gid spring spring

USER spring:spring

# Variável de argumento para localização do JAR
ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} app.jar

# Configurando a execução da aplicação
ENTRYPOINT ["java", "-Xms128M", "-Xmx128M", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
