rwestful-android
================

File storage Read/Write HTTP REST service for Android (HTML5 and App Helper)

Read/Write REST API
-------------------

The goal of this project is to support the following HTTP requests relative to the storage area for rwest-android

GET /action/read/path/to/file.txt

GET /action/grep/path/to/file.txt?arg=content

GET /action/head/path/to/file.txt?n=10

GET /action/tail/path/to/file.txt?n=10

GET /action/write/path/to/file.txt?line=urlencodedstring&newline=true

GET /action/append/path/to/file.txt?line=urlencodedstring&newline=true

POST /action/write/path/to/file.txt

  - Accept JSON
  - Accept Form Data
  - Accept Newline Argument
