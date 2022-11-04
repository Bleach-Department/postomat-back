# Backend

To publish docker images locally run:
```bash
./publish.sh
```

To run the backend:
```bash
docker-compose up
```


## Project Structure

### Submodules
 - `proto` - module with proto files. 
They are in `proto/src/main/proto/*`
 - `stub` - generated stubs from proto files
 - `regions` - service for Regions service
 - `postomat` - service for Postomat service
 - `ktor` - rest server