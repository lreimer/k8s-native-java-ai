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
		--addons HttpLoadBalancing,HorizontalPodAutoscaling \
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

delete-gke-clusters:
	@gcloud container clusters delete k8s-native-java-ai --region=$(GCP_REGION) --async --quiet
