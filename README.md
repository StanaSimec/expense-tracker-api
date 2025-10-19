# Expense Tracker API

### Solution for the [expense-tracker-api](https://roadmap.sh/projects/expense-tracker-api) project from [roadmap.sh](https://roadmap.sh).

## Open project

```
$ git clone https://github.com/StanaSimec/expense-tracker-api.git
$ cd expense-tracker-api
```

## Set up environment variables

- Open src/main/java/resources/application.yml
- Set environment variables:
    - spring.datasource.url
    - spring.datasource.username
    - spring.datasource.passsword
    - jwt.secret
    - jwt.duration

## How to run the app

```
.gradlew/ bootRun
```

## How to register

```
POST api/v1/register
{
    "username": "username",
    "email": "email@mail.com",
    "password": "password"
}
```

## How to login

```
POST api/v1/login
{
    "email": "email@mail.com",
    "password": "password"
}
```

### Returned JWT token insert into Authorization header in form: Bearer ${jwtToken}

## How to create expense

```
POST api/v1/expenses
{
    "name": "Lunch",
    "appliedAt": "2025-11-05",
    "category": "Others"
}
```

### List of predefined categories

- Groceries
- Leisure
- Electronics
- Utilities
- Clothing
- Health
- Others

## How to update expense

```
PUT api/v1/expenses/1
{
    "name": "Lunch",
    "appliedAt": "2025-11-05",
    "category": "Groceries"
}
```

## How to delete expense

```
DELETE api/v1/expenses/1
```

## Find expense
```
GET api/v1/expenses/1
```

## Filter past expenses

- week - last week
- month - last month
- three_months - last three month

```
GET api/v1/expenses?tag=week
```

## Filter expenses by date

```
GET api/v1/expenses?start=2025-06-04&end=2025-06-04
```

## Get expenses for current user

```
GET api/v1/expenses
```

## Tech Stack
- Java
- Gradle
- Spring Boot Starter Web
- Spring Boot Starter JDBC
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Auth0
- PostgreSQL