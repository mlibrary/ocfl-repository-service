# ocfl-repository-service

## Installation/Usage

### Prerequisite(s)

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Configuration

#### Deposit and repository setup

```sh
mkdir data/preservation/storage
mkdir data/preservation/workspace
mkdir data/deposit
```

Then, add a package (directory with any contents) to [data/deposit](data/deposit/).

### Usage

After completing the above, the application should be ready to run as a demo.
The following commands will start up a Web application.

```sh
docker compose build
docker compose up app
```

The application has OpenAPI and Swagger UI support. Once the application has started,
visit http://localhost:8080/swagger-ui/index.html to review
and use the available routes.

Remember to Control + `C` and `docker compose down` to shut down all services when
you are done.

### Testing

The following command will run all tests, both unit and integration.

```sh
docker compose run app gradle test
```

To run individual test classes and get the best performance,
start up the services as normal, then `exec` into the container.
From there, you can run individual
test files like below, replacing `YourTest` with your test class.

```sh
docker compose exec app bash
# In the terminal that appears
gradle test --tests YourTest
```

### Deployment

The provided `Dockerfile` has three stages, which will result in a simple Java
image running a JAR file, designed for use in a deployment setting (e.g. Kubernetes).

To test this image locally, run the following command:

```sh
docker compose up database deployment
```

The JAR file name needs to be set as a build argument.
See [`compose.yaml`](compose.yaml) for an example.

Configuration for deployment will likely leverage the ability to override
`application.properties` values using environment variables.

## Resource(s)

- [Gradle](https://gradle.org/)
- [Spring framework](https://spring.io/)
- [ocfl-java](https://github.com/OCFL/ocfl-java)
