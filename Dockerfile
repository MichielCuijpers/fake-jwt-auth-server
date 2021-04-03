FROM adoptopenjdk/openjdk15:alpine-jre

ARG VERSION

ENV SERVER_PORT=80 \
    AUDIENCE=default-audience \
    ISSUER=default-issuer

COPY build/libs/fake-jwt-auth-server-${VERSION}-all.jar /opt/app.jar

CMD java -jar /opt/app.jar \
    -p ${SERVER_PORT} \
    -a ${AUDIENCE} \
    -i ${ISSUER}