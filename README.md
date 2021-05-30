### Локальная разработка:
1) установить docker: https://docs.docker.com/docker-for-windows/install/
2) перейти в папку deploy и выполнить команду docker-compose up -d (это займёт какое-то время, 
   будут запущены локально последние версии postgres, pgadmin и rabbitmq)
3) убедиться, что установлена версия jdk 11 или выше
4) использовать скрипт local-development для запуска

### Локальная разработка без докера и лишних установок
1) убедиться, что установлена версия jdk 11 или выше
2) использовать скрипт local-development-without-docker для запуска

### Как контейнеризовать приложение:
1) создаём образ: mvn spring-boot:build-image -Dspring-boot.build-image.imageName=casket/admin -Dmaven.test.skip=true
2) пушим в заранее созданный приватный репозиторий: docker push casket/admin:latest

