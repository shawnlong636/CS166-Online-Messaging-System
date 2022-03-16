#!/bin/bash
echo "creating db named ... "$USER"_DB"
createdb -h localhost -p $PGPORT $USER"_DB"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cp $DIR/../../data/*.csv /tmp/$USER/myDB/data/

psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/create_tables.sql
psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/create_indexes.sql
psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/create_triggers.sql
psql -h localhost -p $PGPORT $DB_NAME < $DIR/../src/load_data.sql