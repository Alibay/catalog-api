# create db
~~~
sudo -u postgres psql
CREATE USER catalogdev WITH PASSWORD 'catalogpass';
CREATE DATABASE catalog WITH OWNER = catalogdev ENCODING = 'UTF8';
GRANT ALL PRIVILEGES ON DATABASE catalog TO catalogdev;
~~~

# Решения
~~~
Swagger висит на корневом урле http://localhost:8888/

Категории и продукты в списке получаеются "плоскими", без связных сущностей и только 
в деталях категорий GET /api/v1/categories/{id} возвращается модель, с цепочкой 
родительских категорий, а так же количество вложенных категорий и продуктов на 
первом уровне.

Потенциально, можно было хранить данные в ненормализованном виде (миссивы ID дочерних и
родитесльских категорий, особенно если дерево категорий меняется редко), но это
дополнительные сложности с поддержкой целостности данных
~~~