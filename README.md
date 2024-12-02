# Java-based AI on Kubernetes

Demo repository for Java-based AI on Kubernetes from Development to Deployment.

## Setup

```bash
# create the Kubernetes cluster in GCP with GPU support
# bootstrap AI platform components and services using Flux2
make create-gke-cluster
make bootstrap-flux2
```

## Building a chat service with Quarkus and OpenAI

```bash
# use the Quarkus starter to create a service skeleton
# select desired build system and dependencies
open https://code.quarkus.io

# for local development use the following commands 
cd openai-chat-service
export QUARKUS_LANGCHAIN4J_OPENAI_API_KEY=$OPENAI_API_KEY
./gradlew quarkusDev

# interact with the service locally
http get localhost:8080/api/ask q=="Was macht QAware?"
http get localhost:8080/api/ask q=="What does QAware do?"
http get localhost:8080/api/ask q=="Was macht Microsoft?"
http get localhost:8080/api/ask q=="What is the sum of 40 and 2?"
http get localhost:8080/api/ask q=="What does QAware do? Send email to mlr@qaware.de with subject Information and response as message."

# this here is managed by Flux2
kubectl apply -k infrastructure/services/openai-chat-service/
kubectl get all
```

## Building an OpenAI Proxy using Envoy

The access to the OpenAI API is provided using a cluster internal Envoy based proxy.

```bash
# in order for the proxy to work ou have to manually create a Kubernetes secret
# that contains an OPENAI_API_KEY environment variable
kubectl create secret generic openai-api-key --from-literal=OPENAI_API_KEY=$OPENAI_API_KEY

# to test the proxy, issue the following curl command
# exchange localhost with the actual LoadBalancer IP
curl http://localhost:10000/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
     "model": "gpt-4o-mini",
     "messages": [{"role": "user", "content": "Say this is a test!"}],
     "temperature": 0.7
   }'
```

## Building a chat service with Quarkus and Ollama

```bash
# this is 99% similar to the instructions of using Quarkus and OpenAI
# the only difference, use
#    'io.quarkiverse.langchain4j:quarkus-langchain4j-ollama:0.22.0'
# instead of 
#    'io.quarkiverse.langchain4j:quarkus-langchain4j-openai:0.22.0'

# for local development use the following commands 
ollama serve
ollama run llama3.1

cd ollama-chat-service
./gradlew quarkusDev



# this here is managed by Flux2
kubectl apply -k infrastructure/services/openai-chat-service/
kubectl get all
```

## Deploying custom LLMs using Ollama Operator

```bash
# model deployment using CLI
kollama deploy llama3.1
kollama expose llama3.1 --service-name=ollama-model-llama31-lb --service-type=LoadBalancer

# model deployment via CRD
kubectl apply -f infrastructure/models/phi3.yaml
kollama expose phi3 --service-type LoadBalancer

# to start a chat with ollama
# exchange localhost with the actual LoadBalancer IP
OLLAMA_HOST=localhost:11434 ollama run phi3
OLLAMA_HOST=localhost:11434 ollama run llama3.1

# call the chat API of Ollama
curl http://localhost:11434/api/chat  \
  -H "Content-Type: application/json"  \
  -d '{
    "model": "llama3.1",
    "messages": [
      {
        "role": "user",
        "content": "Say this is a test!"
      }
    ]
  }'
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
