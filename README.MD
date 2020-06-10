# create db
~~~
sudo -u postgres psql
CREATE USER catalogdev WITH PASSWORD 'catalogpass';
CREATE DATABASE catalog WITH OWNER = catalogdev ENCODING = 'UTF8';
GRANT ALL PRIVILEGES ON DATABASE catalog TO catalogdev;
~~~