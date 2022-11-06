# Backend

Для того чтобы опубликовать docker image-ы:
```bash
./publish.sh
```

Запустить бэкэнд:
```bash
docker-compose up
```


## Project Structure

### Submodules
 - `proto` - модуль с proto файлами. 
Они находятся в `proto/src/main/proto/*`
 - `stub` - сгенерированые stub-ы из proto файлов
 - `regions` - имплементация сервиса Regions
 - `postomat` - имплементация сервиса Postomat
 - `ktor` - REST сервер

### Description

Бэкенд написан на микросервисной архитектуре, где каждая из сервисов
отвественна за свою часть: Regions хранит в себе регионы и все операции с регионами
проходят через него, Postomat хранит в себе постоматы, так же Machine Learning имеет в себе свой сервис
отвественный за оценку точки на карте. REST сервер для работы вызывает эти самые сервисы.

