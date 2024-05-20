# Document-Storage-AWS
A User Document Storage Service - S3

Built on the robust foundations of Spring Boot and seamlessly integrated with Amazon Web Services (AWS) S3.

## API Endpoints
### Search Files

Search for files in the user's storage on AWS S3.

-   **URL:** `/documents/search`
-   **Method:** `GET`
-   **Query Parameters:**
    -   `userName`: Name of the user whose files to search
    -   `searchString`: String to search for in file names

### Download File

Download a file from the user's storage on AWS S3.

-   **URL:** `/documents/download`
-   **Method:** `GET`
-   **Query Parameters:**
    -   `userName`: Name of the user who owns the file
    -   `fileName`: Name of the file to download

### Upload File

Upload a file to the user's storage on AWS S3.

-   **URL:** `/documents/upload`
-   **Method:** `POST`
-   **Request Parameters:**
    -   `userName`: Name of the user who owns the file
    -   `file`: File to upload (multipart form-data

### Create User

Create a new user for document storage.

-   **URL:** `/documents/createuser`
-   **Method:** `POST`
-   **Request Parameter :**
    -   `userName`: Name of the user to create

## Configurations
Add your AWS S3 Bucket details (access-key, secret-key, bucket name) in application.yml
