Branch with no proxy scenario.
Each microservice is deployed with a separate host name ms1.myapp.com  ms2.myapp.com  etc.  
There is no proxy to expose microservices as URI myapp.com/ms1   myapp.com/ms2
For browser based UI with javascript based microservice REST/HTTP invocation this creates CORS problems which are addressed in this branch
Tested with Bluemix deployment and Bluemx SSO Service Cloud Directory as OpenID Connect Provider
