## инсутркция:
1. **перейти cd .\docker**
2. **запустить docker-star.cmd**
3. **run application**
4. **документация по http://localhost:8080/swagger-ui/index.html**

## тестировать в postman:
1. создать учетку по http://localhost:8080/api/v1/auth/register
2. получить токен отправив пароль и маил на http://localhost:8080/api/v1/auth/signin
3. полученный бирер токен использовать при запросах на /api/v1/task и /api/v1/review

### пример фильтра
При использовании филтра для поиска таски обязательно укзаывать 
id пользователя а также тип его задач(где он автор или исполнитель), 
размер и номер страницы, остальные фильтры опциональны, пример запроса: 
http://localhost:8080/api/v1/task/filter-by?searchBy=AUTHOR&userId=1&pageNumber=0&pageSize=2

## стек
1. java 17
2. springboot version "3.3.2"
3. liquibase
3. spring security
5. db:
    - postgres
    - fedis
6. развертка - docker
7. для тестов - junit