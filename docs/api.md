# Whisper API

## Auth

Token is required for all requests, except the token API itself and some management endpoints. To get a token:

```sh
curl -XPOST localhost:9090/token -d'{"client_id": "12345", "client_secret": "hellosecret"}' -H"Content-type: application/json"
```

Using `jq`:

```sh
TOKEN=$(curl -XPOST localhost:9090/token -d'{"client_id": "12345", "client_secret": "hellosecret"}' -H"Content-type: application/json" | jq -r '.token') 
```

## Shouts

### List shouts

```sh
curl -H"Authorization: Bearer $TOKEN" localhost:9090/shouts
```

Response:

```json5
{
  "items": [
    {
      "id": 1,
      "message": "Hello world",
      "username": "joerx",
      "timestamp": "2019-10-25T18:18:19.754+0000"
    },
    // ...
  ],
  "page": 1,
  "page_size": 10
}
```

### Get Shout by ID

```sh
curl -H"Authorization: Bearer $TOKEN" localhost:9090/shouts/1
```

Response:

```json5
{
  "id": 1,
  "message": "Hello world",
  "username": "joerx",
  "timestamp": "2019-10-25T18:18:19.754+0000"
}
```

### Create New Shout

- Any ID passed in the payload will be ignored and replaced with an auto-generated one

```sh
curl -XPOST http://localhost:9090/shouts \
    -H"Authorization: Bearer $TOKEN" \
    -H"Content-type: application/json" \
    -d'{"message": "a longer shout", "username": "john"}'
```

Reponse:

```json5
{
  "id": 6,
  "message": "a longer shout",
  "username": "john",
  "timestamp": "2019-11-09T11:30:48.683+0000"
}
```

### Update Shout

- Note: this will completely replace the existing item with the same id
- And ID sent in the payload will be ignored
- This operation is idempotent - sending the same payload multiple times will yield the same result

```sh
curl -XPUT http://localhost:9090/shouts/5 \
    -H"Authorization: Bearer $TOKEN" \
    -H"Content-type: application/json" \
    -d'{"message": "_a fancy shout_", "username": "joerx"}'
```

Response:

```json5
{
  "id": 8,
  "message": "_a fancy shout_",
  "username": "joerx",
  "timestamp": "2019-11-09T11:31:46.278+0000"
}
```

### Delete Shout

- Trying to delete the same item twice will yield a `404 Not Found` on the 2nd attempt 

```sh
curl -H"Authorization: Bearer $TOKEN" -XDELETE http://localhost:9090/shouts/4
```

Response:

```json5
{
  "id": 5,
  "message": "a longer shout",
  "username": "john",
  "timestamp": "2019-11-09T11:30:44.109+0000"
}
```
