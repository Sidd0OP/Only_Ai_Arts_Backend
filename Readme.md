# Only AI Arts

[Only AI Arts](http://onlyaiarts.com/) is a free open source website where users share images they generated using AI models or created by them selves using art creation software. Images that people like increase the popularity of the tool used, which are ranked from 1 to 5. 


## Back Story

Initially I wanted to create a website for my simulator [train traffic sim](https://github.com/Sidd0OP/A-Simple-Game-About-Scheduling-Trains) which was to be a community forum. Also users could upload Maps and train traffics for other users, but later realized nobody was using my simulator ☹️ so I decided to not work on the website (though a basic implementation was ready) .

Then I got the idea 💡to make an Arena for AI art, similar to LMArena, people would upload images but the likes, comments and shares would determine the rank of the model. 

Now most of the work on the website is done, I am adding more features time to time and improving it and it would be helpful if you can contribute also 💖(cuz its a lot of work 😭)

## Setup


Clone the repo, import as a **Maven Project**

```bash 
https://github.com/Sidd0OP/Only_Ai_Arts_Backend.git
```  

The application relies on **API keys** fed via environment variables to to run , I recommend creating a script to run this application like ```run_server.bat```

For running the **Docker** image **.env** file is required which also requires the following data.

```bash
@echo off

#Create a cloudflare r2 bucket 
set cloudflare_acessKey=*******************
set cloudflare_bucket=*******************
set cloudflare_endpoint=*******************
set cloudflare_publicUrl=*******************
set cloudflare_secretKey=*******************
set cloudflare_token=*******************

set database_url=jdbc:postgresql://127.0.0.1:5432/Forum?user=*******************&password=*******************

#Create Oauth keys or leave them blank
#If you create your own keys then you also need to update the client key in vue project 
set google_client_id=*******************
set google_client_secret=*******************

#Leave them blank
set email_host=*******************
set email_password=*******************
set email_username=*******************

#Now build the Jar
mvn clean install
```

## Database 

The schema of the DB is here 👉 [schema](https://drawsql.app/teams/patiala/diagrams/simulator-forum) (not fully accurate)

- The project uses **PostgreSQL 17** for DB get here [download](https://www.postgresql.org/download/)

- Use the backup file **db_backup_base.sql** to create the database 

- Update the DB **Password** and **User** in the run script

## Future Goals

### Security

( I hardcoded API keys into the project and pushed it, twice btw)

- I really don't trust the Remember-Me tokens and the OAuth system I implemented really scares me 🥶

- I check almost 🥲all DB calls for exceptions and don't for some so fixing that would be appreciated. 

- Some **API** calls don't check the text input size , need to do that 📝

- Need to properly check the Images uploaded, don't want someone uploading a text file as .png
- Please implement CSRF, it scares me 😭 

### Features

- Write tests , yes please I feel too lazy to do that.

- Need to add chat , notification and follow options

- Use signed URL's which do the same  Image modification and checks as the server to Improve upload speed 
- Please checkout the workflow file, I know it sucks please recommend improvements 
- If you have more ideas then do it ✅


### The website is hosted on Azure VM, Nginx serves the requests and  GitHub Actions is the CI/CD 











