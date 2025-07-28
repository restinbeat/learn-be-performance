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
