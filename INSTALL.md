To set up and run the project, follow these instructions:

## Step 1: Clone the Repository

    git clone [github_repo_url]

## Step 2: Configure Environment Variables
   Rename the .env-example file to .env and provide the necessary values for the following keys:

   POSTGRES_USERNAME=your_username
   POSTGRES_PASSWORD=your_password
   POSTGRES_PORT=your_postgres_port
   PORT=your_application_port

## Step 3: Start Docker Compose
   Run the following command to start the application using Docker Compose:
   Run command docker-compose --env-file .env up -d

## Step 4: Access the Application
   Open your preferred API testing tool, such as Postman, and send requests to the following endpoint:
   http://localhost:8080/job-item?job-function=your_selected_job_function

   Additionally, you can retrieve a list of all job functions by making the following request:
   http://localhost:8080/job-function/all