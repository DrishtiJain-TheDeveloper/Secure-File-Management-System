#!/bin/sh
# Maven wrapper script for Unix
MAVEN_WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"

if [ -f "$MAVEN_WRAPPER_JAR" ]; then
    exec java -jar "$MAVEN_WRAPPER_JAR" "$@"
fi

# Download wrapper jar if missing
DOWNLOAD_URL=$(grep 'wrapperUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2)
mkdir -p .mvn/wrapper
curl -o "$MAVEN_WRAPPER_JAR" "$DOWNLOAD_URL" 2>/dev/null || wget -O "$MAVEN_WRAPPER_JAR" "$DOWNLOAD_URL"
exec java -jar "$MAVEN_WRAPPER_JAR" "$@"
