# Vaultionizer backend

Hey, welcome to the Github repository for the backend for the [Vaultionizer Android application](https://github.com/Vaultionizer/vault-android-app).

The application aims at enabling a user to securely store data online without having to fear the data being analyzed, breached or anything similar. Our goal is to put zero trust into server instances and instead rely solely on the user knowing what to do.

Before reading any further, note that this application is explorative and should not be used in practice (since for debugging purposes, the data is not encrypted yet)!

## Disclaimer: This application is not ready for use! The data is not secured yet and to this point can easily be read by the server instance. Therefore, you should *really* not use it.

## What does the server instance know about the users?
Basically "nothing". More precisely, the server knows about the relations between users (who created which space, how many files were uploaded and theoretically usage information). All files should (at some point) be encrypted with a key that is only known by the users of the space. To achieve that, we rely on the user knowing how to exchange keys securely (= in person or via another "secure" channel).
All semantic data (like filenames, contents etc.) is stored in a custom (JSON-based, sorry for that) format.

## Usage of the backend
The backend can easily be deployed using Docker and then used by the Android application.

## Swagger-API
For the current API, see [here](https://v2202006123966120989.bestsrv.de/swagger-ui.html#/).
