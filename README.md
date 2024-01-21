# Task Tracker

Hii My name is Rajan kumar currently persuing bachelors degree from NIT Patna with Computer Science and Engineering Department.

Please find my profile here

<ul>
<li>Resume <a href="https://drive.google.com/file/d/1mk62t09UnlHCtr5EL1lSXUDwq1FHSSSO/view" target="_blank">Rajan_kumar_resume</a></li>
<li>Portfolio <a href="https://krcpr007.vercel.app/" target="_blank">Portfolio </a> </li>
<li>Linkedin <a href="https://www.linkedin.com/in/krcpr007/" target="_blank">Linkedin </a></li>
<li>Github <a href="https://github.com/krcpr007"  target="_blank">Github </a></li>

</ul>
 


<p>To Run this project you have to install <b>docker</b> because i have used postgresql database in a docker container. </p>




Start Docker and 

Run the following command


```
docker run -d -p 5432:5432 --name leap -e POSTGRES_PASSWORD=Rajan@123 -e POSTGRES_DB=leap-db postgres
```
Or
Run the container which i have created in docker-compose.yml with following command
```
docker compose up -d

or

docker-compose up -d
```
Run the following command to use postgresql CLI of leap-db

```
docker exec -it leap psql -U postgres -d leap-db
```
Now run following command ( for inserting the two values in the roles table)
```
INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
```
## Setup Project on local system

### Clone this repository 
Run the following command to clone this repo
```
git clone https://github.com/krcpr007/task-tracker-leap.git
cd task-tracker-leap
```

### Run Backend

Go to the task-tracker-backend directory with following command 
```
cd task-tracker-backend
mvn clean
mvn install 
mvn spring-boot:run
```
#### Backend runs on localhost port 8080

### Run Frontend
Open a new terminal and run the following command 
```
cd task-tracker-fronted
npm install -f
npm run start
```
#### Frontend run on localhost port 3000
