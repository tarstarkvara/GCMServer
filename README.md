# GCMServer - Succesfully deployed to heroku, atm trying to get it running properly.

current error on trying to start app

2015-10-20T21:18:41.097671+00:00 heroku[web.1]: State changed from crashed to st
arting
2015-10-20T21:18:44.445675+00:00 heroku[web.1]: Starting process with command `j
ava -XX:+UseCompressedOops -jar GCMServer/target/dependency/* --port 18664 GCMSe
rver/target/*.war`
2015-10-20T21:18:46.468186+00:00 app[web.1]: Error: Unable to access jarfile GCM
Server/target/dependency/*
2015-10-20T21:18:46.464901+00:00 app[web.1]: Setting JAVA_TOOL_OPTIONS defaults
based on dyno size. Custom settings will override them.
2015-10-20T21:18:47.222623+00:00 heroku[web.1]: State changed from starting to c
rashed
2015-10-20T21:18:47.204806+00:00 heroku[web.1]: Process exited with status 1
2015-10-20T21:18:48.628077+00:00 heroku[router]: at=error code=H10 desc="App cra
shed" method=GET path="/" host=peaceful-eyrie-4220.herokuapp.com request_id=0009
1c88-5a67-4aca-86a3-4de458ea912b fwd="80.235.82.94" dyno= connect= service= stat
us=503 bytes=
2015-10-20T21:18:49.489778+00:00 heroku[router]: at=error code=H10 desc="App cra
shed" method=GET path="/favicon.ico" host=peaceful-eyrie-4220.herokuapp.com requ
est_id=326ff29d-b91a-4039-b7fd-78fa4d83e3d2 fwd="80.235.82.94" dyno= connect= se
rvice= status=503 bytes=

C:\Users\Sander\GCMServer>