FROM europe-docker.pkg.dev/viteezy/run-dropwizard/docker

COPY target/backend-1.0-SNAPSHOT.jar /app/app.jar
COPY config.yml /app/config.yml

EXPOSE 8080
EXPOSE 8081
