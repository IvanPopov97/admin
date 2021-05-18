### Локальная разработка:
1) установить docker: https://docs.docker.com/docker-for-windows/install/
2) перейти в папку deploy и выполнить команду docker-compose up -d (это займёт какое-то время, 
   будут запущены локально последние версии postgres, pgadmin и rabbitmq)
3) убедиться, что установлена версия jdk 11 или выше
4) использовать скрипт local-development для запуска

### Как containerize приложение:
Выполнить Maven команду: mvnw spring-boot:build-image.
Далее пушим образ в docker hub и делаем pull на сервере.
Это можно автоматизировать с помощью CI/CD.
