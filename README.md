# Naima

Naima is a Discord bot for listening to albums with your friends! It stores album suggestions in a database and lets you vote on a random selection of them to decide what to listen to. 

## Setup

Currently Naima needs to be self-hosted and will require a decent amount of technical knowledge. You'll need to create your own bot in the Discord developer portal, and make a `.env` file that contains all the values required by `Environment.kt` in Naima's source code, as well as all the values required for the MongoDB container to run correctly. Once that's done, just run `build_run.sh` and you're off to the races.

## Usage

### `/submit`

Use this command to submit an album to Naima's database.

### `/open`

Use this command to open a voting round.

### `/close`

Use this command to close a voting round and determine which album to listen to next!
