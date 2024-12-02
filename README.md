# Kk8s-native-java-ai

Demo repository for Java-based AI on Kubernetes from Development to Deployment.

## Setup

```bash
# create the Kubernetes cluster in GCP with GPU support
# bootstrap AI platform components and services using Flux2
make create-gke-cluster
make bootstrap-flux2
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

## Deploying custom LLMs using Kollama Operator

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

# to integrate with your OpenAI API compatible client
curl http://localhost:11434/v1/chat/completions  \
  -H "Content-Type: application/json"  \
  -d '{
    "model": "phi3",
    "messages": [
      {
        "role": "user",
        "content": "Hello!"
      }
    ]
  }'
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
