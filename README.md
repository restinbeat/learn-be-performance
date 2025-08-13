# learn-be-performance

- 백엔드 성능 개선

### install artillery
```bash
$ npm install -g artillery@latest
```

### run mysql
```bash
$ docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=somepassword -e MYSQL_DATABASE=shortenurl -p 3306:3306 -d mysql:latest
```

### test artillery
```bash
# 테스트 스크립트 실행 및 결과 저장
artillery run create-load-test.yaml -o create-load-report.json

# HTML 보고서 생성
artillery report --output create-load-report.html create-load-report.json
```

### 튜닝 비교

#### POST

- default
![step_post_0.png](src/main/resources/static/images/step_post_0.png)
- connection pool 1
![step_post_1.png](src/main/resources/static/images/step_post_1.png)
- key generator (snowflake)
![step_post_2.png](src/main/resources/static/images/step_post_2.png)
#### GET
![step_get_0.png](src/main/resources/static/images/step_get_0.png)
