FROM openjdk:21-ea-24-oracle

WORKDIR /app
COPY target/ms_registros_usuarios-0.0.1-SNAPSHOT.jar app.jar
COPY Wallet_F8BFPCSH45PSLN97 /app/oracle_wallet
EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]