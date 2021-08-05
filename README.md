# cio-chnlsales-microservice-starter
**TODO: Cloned from cio-notification-microservice - customize for NGC Team.  Submit PR as needed.**

Once repository is created from template modify the following files from the sample-starter - i.e. prefix it my-sample-starter

```
cloudbuild.yaml - change _SERVICE_NAME
helm/values-np.yaml - change applicationName, image.repository
cicd/data.json - change application
```


This is a template project to create REST service on GKE. The app is impelmented using Java 11, and Springboot 2.2.1.
Base image: https://github.com/telus/cio-notification-devops/tree/master/docker/jdk11

## Code Structure
    .
    ├── etc                          # Application configuration 
    ├── helm                         # Helm Chart value file
    ├── src                          # Source files
    ├── Dockerfile                   # Docker file for building the image
    ├── cloudbuild.yaml              # Cloud build file used by GCP CloudBuild CI pipeline
    ├── spinnaker.json               # spinnaker pipeline json file
    └── README.md

## Deployment Template
Helm is used to generate the deployment yaml file.
The base chart can be found here: https://github.com/telus/cio-notification-devops/tree/master/helm/ecp-springboot-deployment

To reference it in the spinnaker: gs://cio-gke-devops-e4993356-kubernetes-manifests/notify-base/ecp-springboot-deployment-chart.v.1.0.8.tgz

Available versions: https://github.com/telus/cio-notification-devops/releases

At the time of writing, we dont have access to the chart storage. In order to test the deployment template rendering, you can checkout the base chart repo and modify the value file and run 

```
helm template .
```

Currently we have not automated the process of deploying the service account, if your application needs access to particular GCP service project, the service account needs to be manually deployed to the K8S secret. The service project and account needs to be specificed at the ecp springboot base helm chart. The base chart will mount the service account to path `/secrets/serviceAccount/{{ .Values.serviceProject.svcAccountJsonName }}` and export the path as environment variable `GOOGLE_APPLICATION_CREDENTIALS`

## API Doc
Swagger ui and Bean validation JSR 380 is added to the project, this will allow you to document & generate swagger from code.
You can access the swagger document using this url: http://localhost:8080/swagger-ui.html#/ running locally.
Try using validation annotation for the input payload validation, as this will reflect directly to the swagger document.

## Configuration
- Use secret manager for any username/password
```java
 @Autowired 
 private SecretManagerTemplate secretManagerTemplate;
 ...
 secretManagerTemplate.getSecretString("secretName");
```
You can also use ```@ConfigurationProperties``` to automatically bind the property source from secret manager.

- Spring Config Server  
Application configuration needs to be created on the spring config server. for detailed usage, refer to https://github.com/telus/cio-notification-config

## Docker Image

If your application needs to call internal SOA services, you can choose to use `gcr.io/cio-gke-devops-e4993356/notification-devops/jdk11/adoptopenjdk:latest` as your base image. Source can be found here https://github.com/telus/cio-notification-devops/tree/master/docker. This image already have the SOA certificate imported to the trust store.

If your application does not need to call any internal SOA services, you can choose any images you want. However, you should consider choosing an image that is portable and lightweight. The size of the image doesn't matter much in the storage space, but it is important aspect through the container lifeycle deployed on kubernates. When an container is using large base image, it means it will have higher latency for the container to be restarted.

Alpine version is a good choice for base image, however at the time of writing there is no official Alpine version for java 11.
You can use unofficial version of the adoptopenjdk `adoptopenjdk/openjdk11:alpine-jre`, or build your own images using jlink/Modules.



