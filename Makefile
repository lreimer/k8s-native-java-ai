# https://app.electricitymaps.com/map?lang=de
# https://cloud.google.com/compute/docs/regions-zones?hl=de#available
GCP_PROJECT ?= cloud-native-experience-lab
GCP_REGION ?= europe-west4

GITHUB_USER ?= lreimer

create-gke-cluster:
	@gcloud container clusters create k8s-native-java-ai \
		--release-channel=regular \
		--cluster-version=1.30 \
		--region=$(GCP_REGION) \
		--addons HttpLoadBalancing,HorizontalPodAutoscaling,ConfigConnector \
		--workload-pool=$(GCP_PROJECT).svc.id.goog \
		--num-nodes=1 \
		--min-nodes=1 --max-nodes=5 \
		--enable-autoscaling \
		--autoscaling-profile=optimize-utilization \
		--enable-vertical-pod-autoscaling \
		--machine-type=n1-standard-8 \
		--accelerator type=nvidia-tesla-t4,count=1 \
		--local-ssd-count=1 \
		--logging=SYSTEM \
    	--monitoring=SYSTEM
	@kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$$(gcloud config get-value core/account)
	@kubectl cluster-info

bootstrap-flux2:
	@flux bootstrap github \
		--owner=$(GITHUB_USER) \
		--repository=k8s-native-java-ai \
		--branch=main \
		--path=./infrastructure/cluster \
		--components-extra=image-reflector-controller,image-automation-controller \
		--read-write-key \
		--personal

# Create a Service Account for Airbyte storage and secrets
create-gke-airbyte-sa:
	@gcloud iam service-accounts create airbyte --description="Airbyte Service Account" --display-name="Airbyte Service Account"
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/storage.admin --member=serviceAccount:airbyte@$(GCP_PROJECT).iam.gserviceaccount.com 
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/storage.objectCreator --member=serviceAccount:airbyte@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.secretAccessor --member=serviceAccount:airbyte@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.secretVersionAdder --member=serviceAccount:airbyte@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.secretVersionManager --member=serviceAccount:airbyte@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.viewer --member=serviceAccount:airbyte@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud iam service-accounts keys create airbyte.json --iam-account=airbyte@$(GCP_PROJECT).iam.gserviceaccount.com

# Create a Service Account for External Secrets
# Service Account will also be used for Workload Identity
create-gke-es-sa:
	@gcloud iam service-accounts create external-secrets-sa --description="External Secrets Service Account" --display-name="External Secrets Service Account"
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.secretAccessor --member=serviceAccount:external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.secretVersionAdder --member=serviceAccount:external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.secretVersionManager --member=serviceAccount:external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/secretmanager.viewer --member=serviceAccount:external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud projects add-iam-policy-binding $(GCP_PROJECT) --role=roles/iam.serviceAccountTokenCreator --member=serviceAccount:external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com
	@gcloud iam service-accounts add-iam-policy-binding external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com --member="serviceAccount:$(GCP_PROJECT).svc.id.goog[external-secrets/external-secrets-sa]" --role="roles/iam.workloadIdentityUser" 
	@gcloud iam service-accounts keys create external-secrets-sa.json --iam-account=external-secrets-sa@$(GCP_PROJECT).iam.gserviceaccount.com

delete-gke-clusters:
	@gcloud container clusters delete k8s-native-java-ai --region=$(GCP_REGION) --async --quiet
