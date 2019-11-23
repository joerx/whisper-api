# Whisper API

API gateway for whisper

## API Usage

See [API docs](docs/api.md)

## Integration Tests

Integration tests are not run by default since they require all Whisper components to be running. For now, there is no
fully automated way to do this.

Get [whisper-compose](https://github.com/joerx/whisper-compose/) and start all components:

```sh
docker-compose up
```

Then run Maven with the `integration-tests` profile:

```sh
./mvnw test -Dintegration-tests
```
