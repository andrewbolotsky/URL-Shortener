# URL Shortener

### Description

REST API service to generate shortened URLs.

### API Format

#### GET /shorten_url

Gets a shortened URL from the database if it exists there. Otherwise, the server returns an error.

##### Interaction Format:

Request:
```json
{
  "url": "your_url"
}
```

Response:
```json
{
  "shortenedUrl": "your_shortened_url"
}
```

#### POST /shorten_url

Gets a shortened URL from the database if it exists there or saves a new shortened URL in the database. 

##### Interaction Format:

Request:
```json
{
  "url": "your_url"
}
```

Response:
```json
{
  "shortenedUrl": "your_shortened_url"
}
```


#### GET /\<your_shortened_key\>

Does not have a body. Redirects you to the URL which was shortened if <your_shortened_key> is correct and saved in the database.

### Algorithm of Generating Shortened Keys

The program gets the MD5 hash of your URL and a random string, then gets a random 7 characters from this hash (characters are numbers 0..9 and lowercase Latin letters). The probability of coinciding keys is very small because this algorithm can generate 78 billion different keys.
