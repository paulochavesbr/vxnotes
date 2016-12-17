# vxnotes
A REST API for a Notes App built with Vert.x3 and MongoDB.

- It comes with Vert.x Web, Slf4j, Logback, Mongo client and unit testing
- In contains CORS support out of the box

## To create a fat jar

```sh
mvn clean package
```

## To run the app from the command line

```sh
java -jar target/vxnotes-{version}-fat.jar -conf src/main/resources/config.json
```

## To run the app with redeploy from your IDE (Eclipse in my case)

1. Go to Run > Run Configurations > Java Application > Right click > New
2. On tab **Project** select your project on **Project** text field and in the field **Main class** add `io.vertx.core.Launcher`
3. On tab **Arguments** paste the following in the **Program arguments** text box and click **Apply**

```
run io.github.paulochavesbr.vxnotes.WebServer -conf src/main/resources/config.json --redeploy="src/**/*.java" --launcher-class=io.vertx.core.Launcher
```