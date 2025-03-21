docker run --rm -d \
  --name arguments-dev-mongodb \
  -p 27017:27017 \
  mongo:latest

docker run -d \
  --name arguments-dev-ollama \
  -p 11434:11434 \
  ollama/ollama 2%>/dev/null

docker exec -d arguments-dev-ollama ollama pull llama3.2:1b