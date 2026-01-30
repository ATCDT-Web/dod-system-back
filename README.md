## Технологии

Язык: Java (Spring Boot)   
API Документация: OpenAPI 3.1 (Swagger)   
База данных: PostgreSQL   
Контейнеризация: Docker   
Аутентификация: JWT Token   

## Запуск
```text
 docker-compose up --build
```


## Запросы auth
### /api/auth/register - регистрации (POST)
Request body
```json
{
  "name": "string",
  "email": "string",
  "password": "string",
  "district": "string",
  "educationalInstitution": "string",
  "position": "string",
  "phone": "string",
  "address": "string"
}
```
Response
```text
User registered successfully
```

### /api/auth/login - авторизация (POST)
Request Body
```json
{
  "email": "string",
  "password": "string"
}
```
Response
```json
{
  "token": "token",
  "email": "string",
  "name": "string",
  "isAdmin": false,
  "type": "string"
}
```
## Запросы unit
### /api/unit/initReference - создания справки (POST), все разделы создадутся автоматически
Request body
```json
{
  "organizationName": "string",
  "postalAddress": "string",
  "okudFormCode": "string",
  "okpoOrgCode": "string",
  "changeDate1": "2026-01-27T20:32:18.485Z",
  "changeNumber1": "string",
  "changeDate2": "2026-01-27T20:32:18.485Z",
  "changeNumber2": "string",
  "status": "string",
  "rejectionReason": "string"
}

```

### /api/unit/getMainInfoList - получение списка организаций с пагинацией GET

Параметры: pageable   
Пример запроса:
```text
GET /api/unit/getMainInfoList?page=0&size=10
```
Ответ:
```json

{
  "content": [
    {
      "changeDate1": "2026-01-27T00:00:00.000Z",
      "changeDate2": "2026-01-27T00:00:00.000Z",
      "changeNumber1": "string",
      "changeNumber2": "string",
      "id": 1,
      "okpoOrgCode": "string",
      "okudFormCode": "string",
      "organizationName": "string",
      "postalAddress": "string",
      "rejectionReason": "string",
      "reportTitle": null,
      "status": "string"
    }
  ],
  "empty": false,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 1,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 5,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "unpaged": false
  },
  "size": 5,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "totalElements": 1,
  "totalPages": 1
}

```


### /api/unit/updateUnit18 - обновление раздела (PUT)
Request Body
```json
{
    "id": 1,
    "internalDigitalTechCostsTotal": 150000,
    "ownFunds": 50000,
    "budgetFunds": 80000,
    "otherAttractedFunds": 20000
}
```
Response
```text
Updated
```
### /api/unit/getUnit18 - получения раздела (GET)
Параметры: id (required)   
Ответ (Unit18):
```json
{
    "internalDigitalTechCostsTotal": 150000,
    "ownFunds": 50000,
    "budgetFunds": 80000,
    "otherAttractedFunds": 20000
}
```
### /api/unit/getReportUnit5 - получение суммы по разделу по имени организации (GET)
Параметры: organizationName   
Ответ: 
```json
{
  "technical": [0,0,0,0,0],
  "naturalScience": [0,0,0,0,0],
  "tourismAndLocalHistory": [0,0,0,0,0],
  "socialAndHumanitarian": [0,0,0,0,0],
  "artisticOrientation": [0,0,0,0,0],
  "physicalEducationAndSports": [0,0,0,0,0],
  "preprofessionalProgramsInTheFieldOfArts": [0,0,0,0,0],
  "additionalEducationalProgramsSportsTraining": [0,0,0,0,0]
}
```

### api/unit/export/unit7/{organizationName} - получение отчета по сумме в excel формате (GET)
Ответ: excel файл

### api/unit/export/unit/{unit}/district/{district} - получение отчета по разделу (GET)
Ответ: excel файл

### api/unit/delete/{reportId} - удаление отчета (DELETE)
Ответ: Deleted

### api/unit/importExcel - импорт эксель таблицы (POST)
Params: reportId, mode
RequestBody: excel файл

## Запросы user 
### api/user/update/{id} - обновление пользователя (UPDATE)
Request Body
```json
{
  "name": "string",
  "email": "string",
  "password": "string",
  "district": "string",
  "educationalInstitution": "string",
  "position": "string",
  "admin": true
}
```
Response
```json
{
  "id": 0,
  "name": "string",
  "email": "string",
  "district": "string",
  "educationalInstitution": "string",
  "position": "string",
  "phone": "string",
  "address": "string",
  "enabled": true,
  "admin": true,
  "authorities": [
    {
      "authority": "string"
    }
  ],
  "username": "string",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true
}
```

### api/user/{id} - получение пользователя (GET)

Response
```json
{
  "id": 0,
  "name": "string",
  "email": "string",
  "district": "string",
  "educationalInstitution": "string",
  "position": "string",
  "phone": "string",
  "address": "string",
  "enabled": true,
  "admin": true,
  "authorities": [
    {
      "authority": "string"
    }
  ],
  "username": "string",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true
}
```

### api/user/getAllUsers - получение всех пользователей (GET)

Response
```json
[
  {
    "id": 0,
    "name": "string",
    "email": "string",
    "district": "string",
    "educationalInstitution": "string",
    "position": "string",
    "phone": "string",
    "address": "string",
    "enabled": true,
    "admin": true,
    "authorities": [
      {
        "authority": "string"
      }
    ],
    "username": "string",
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true
  }
]
```

### api/user/delete/{id} - удаление пользователя (DELETE)



