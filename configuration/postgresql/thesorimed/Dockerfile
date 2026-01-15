FROM postgres:17.6-alpine3.22

ENV POSTGRES_DB=thesorimed_db
ENV POSTGRES_USER=postgres

COPY init.sql /docker-entrypoint-initdb.d/01-init.sql
COPY thesorimed.backup /docker-entrypoint-initdb.d/02-thesorimed.backup
COPY restore.sh /docker-entrypoint-initdb.d/03-restore.sh
COPY THESO-postgres-1300-2_43.sql /docker-entrypoint-initdb.d/04-THESO-postgres-1300-2_43.sql
COPY search_path.sql /docker-entrypoint-initdb.d/05-search-path.sql

RUN sed -i 's/\r$//' /docker-entrypoint-initdb.d/03-restore.sh \
 && chmod +x /docker-entrypoint-initdb.d/03-restore.sh

EXPOSE 5432