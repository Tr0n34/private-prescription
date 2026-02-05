#!/bin/sh
set -e

echo "Restoring custom dump (.backup) into $POSTGRES_DB ..."

pg_restore \
  -U "$POSTGRES_USER" \
  -d "$POSTGRES_DB" \
  --no-owner --no-privileges \
  /docker-entrypoint-initdb.d/02-thesorimed.backup