docker image build -t rest_dietiestates25 .
docker tag rest_dietiestates25 dietiestatesrestapi.azurecr.io/rest_dietiestates25
docker push dietiestatesrestapi.azurecr.io/rest_dietiestates25