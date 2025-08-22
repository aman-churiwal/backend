# Install the archetype in your local maven repo

```
cd ~/dev/../viteezy/backend/module-archetype
mvn clean install
```

# Generate a module throughout the installed archetype

## 1 Run the generation
```
cd /tmp
rm -r backend;
mvn archetype:generate \
    -DarchetypeGroupId=viteezy -DarchetypeArtifactId=module-archetype -DarchetypeVersion=1.0-SNAPSHOT \
    -DgroupId=viteezy -DartifactId=backend -Dpackage=viteezy \
    -DmoduleNamePascalCase=Allergy -DmoduleNameCamelCase=allergy -DmoduleNameSnakeCase=allergy -DmoduleNameKebapCase=allergies \
    -Dversion="1.0-SNAPSHOT" \
    -DsqlSchemaVersion=1 -DsqlTableName=allergies -DsqlAnswerTableName=allergy_answers -DsqlAnswerSchemaVersion=2;
subl backend;
```

# 2 Perform the schema migration

```
IAC_FOLDER=~/dev/viteezy/iac
rsync -avh /tmp/backend/sql/viteezy/schemas $IAC_FOLDER/_db/flyway/backend;
```

# 3 Fill the data folder with real data

(Manual action)

# 4 Perform the data migration

```
IAC_FOLDER=~/dev/viteezy/iac
rsync -avh /tmp/backend/sql/viteezy/data $IAC_FOLDER/_db/flyway/backend;
```

# 5 (Optional) improve sql files naming

Due to both flyway and maven archetype using `__` (double underscore) as a separator, they conflict and the most
appropriate name pattern for schema migrations cannot be used (`V__sqlSchemaVersion__create__sqlTableName__.sql`)

# 6 Commit the code for iac

(manual action)

# 7 Copy the java code to the project

```
BACKEND_FOLDER=~/dev/viteezy/backend
rsync -avh /tmp/backend/src $BACKEND_FOLDER; # Automatically copies the new module into the existing project
```

# 8 Fix the code

+ Caches
+ Ioc
+ Delete generated template files

# 9 Commit the code for backend

(manual action)

