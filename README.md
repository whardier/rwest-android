# rwestful-android

General Purpose HTTP based helper application providing local device access to HTML5 applications (online and offline) being accessed locally on a device.

The core goal of this project is to help applications of any nature quickly access file related information stored on external storage facilities (sdcards, etc...)

## Common API Arguments

The API supports GET based requests for now (POST will require a better multi-part parser)

### Common GET parameters (* multiple arguments allowed)

  - line string * - Used when writing or appending textual lines to files
  - data base64 * - Base64 encoded binary data used when writing or appending binary data to files
  - pattern string * - Regular expression pattern used for searching textual line data
  - format string - Can be 'raw' (default) for raw file access or 'json'/'jsonp' (returns arrays or chunks)
  
### Line function GET parameters

  - newline true/false - Remove existing newlines per line argument and replace with a single newline '\n'
  - count integer - Typically used for line count information

## API Endpopint Structure

Typically {function}/{method}/{path or glob}?parameters

## Storage API Endpoints

### GET /storage/read/path/to/file.txt

Read a file and return it

### GET /storage/grep/path/to/file.txt?pattern=content

Return the lines of a file that match

### GET /storage/head/path/to/file.txt?count=10

Return the first 'count' lines in a file

### GET /storage/tail/path/to/file.txt?count=10

Return the last 'count' lines in a file

### GET /storage/since/path/to/file.txt?(count=10)

Return the last 'count' lines in a file since the last 'since' call on a file.

### GET /storage/write/path/to/file.txt?(line=string&newline=true|data=base64encodeddata)

Create or overwrite '/path/to/file.txt' and replace with line or data content.

### GET /storage/append/path/to/file.txt?(line=string&newline=true|data=base64encodeddata)

Create or append lines or data to the end of a new or existing file.
