## Running the sample app

Create a google cloud account and create a data store instance/project.

Create a service account and download the credentials.json.
  - Instructions here: https://cloud.google.com/datastore/docs/activate
  - Also enable the Datastore API (required for accessing datastore out side of google cloud)

### Indexes

Indexes must be created e.g.

```
gcloud datastore create-indexes index.yaml 
```

You'll need the google cloud [cli](https://cloud.google.com/sdk/downloads)

### Config

In application.conf put in your
- project
- location of credentials.json

## Datastore clients

Google datastore client
- Synchronous API

Google app engine client
- Synchronous API
- Future API

Spotify
- Asynchronous API and network (async http client)

## TODO
* Look into local testing with the gcloud simulator

