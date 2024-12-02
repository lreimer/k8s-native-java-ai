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

```bash
# in order for the proxy to work ou have to manually create a Kubernetes secret
# that contains an OPENAI_API_KEY environment variable
kubectl create secret generic openai-api-key --from-literal=OPENAI_API_KEY=$OPENAI_API_KEY
```


## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
