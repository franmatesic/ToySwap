# Setup

- Download and install docker desktop (if not already installed)
- Execute `docker-compose up` in terminal in this directory

# Running the application

- Execute `docker-compose start` in terminal in this directory (if docker container not running)
- Start `ToySwapApplication`
- Open [`localhost:8080`](http://localhost:8080) in browser

# Users

| Username          | Password | Role  |
|-------------------|----------|-------|
| fran@gmail.com    | pass     | USER  |
| admin@toyswap.com | pass     | ADMIN |

# REST Api

## LOGIN

POST `/api/login`

REQUEST EXAMPLE

```json
{
  "email": "example@mail.com",
  "password": "password"
}
```

RESPONSE EXAMPLE

```json
{
  "token": "eyJhbGciOiJIUzU...",
  "user": {
    "email": "example@mail.com",
    "firstName": "First name",
    "lastName": "Last name",
    "role": "USER",
    "phoneNumber": "0912345678",
    "enabled": true,
    "profilePicture": null
  }
}
```

## REGISTER

POST `/api/register`

REQUEST EXAMPLE

```json
{
  "email": "example@mail.com",
  "password": "password",
  "firstName": "First name",
  "lastName": null,
  "phoneNumber": "0912345678",
  "profilePicture": null
}
```

## POSTS

POST `/api/posts`

RESPONSE EXAMPLE

```json
[
  {
    "id": 1,
    "title": "Title",
    "description": "Description",
    "condition": "NEW",
    "price": 12.99,
    "tags": [
      {
        "name": "LEGO"
      },
      {
        "name": "BUILDING"
      }
    ]
  }
]
```
