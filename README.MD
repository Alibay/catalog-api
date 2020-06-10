# create db
~~~
sudo -u postgres psql
CREATE USER goremotedevuser WITH PASSWORD 'secret';
CREATE DATABASE goremotedev WITH OWNER = goremotedevuser ENCODING = 'UTF8';
GRANT ALL PRIVILEGES ON DATABASE goremotedev TO goremotedevuser;
~~~