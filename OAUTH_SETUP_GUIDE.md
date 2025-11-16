# Google OAuth2 Setup Guide for Production

## Issue

Google OAuth2 is not working after hosting the website because Google needs to whitelist your production URLs.

## Backend Configuration (Already Done ✓)

Your backend is correctly configured:

- OAuth endpoint: `POST /user/api/google-login`
- Client ID is read from environment variable: `GOOGLE_CLIENT_ID`
- CORS is configured to allow your S3 frontend

## Required Google Cloud Console Updates

### 1. Go to Google Cloud Console

- Visit: https://console.cloud.google.com/
- Select your project

### 2. Navigate to OAuth Consent Screen

- Go to **APIs & Services** → **OAuth consent screen**
- Verify your app details are correct

### 3. Update Authorized Origins and Redirect URIs

Go to **APIs & Services** → **Credentials** → Click on your OAuth 2.0 Client ID

#### Add Authorized JavaScript Origins:

```
http://social-media0282.s3-website.ap-south-1.amazonaws.com
http://ec2-3-110-55-80.ap-south-1.compute.amazonaws.com:8080
http://ec2-3-110-55-80.ap-south-1.compute.amazonaws.com
```

#### Add Authorized Redirect URIs:

```
http://social-media0282.s3-website.ap-south-1.amazonaws.com
http://social-media0282.s3-website.ap-south-1.amazonaws.com/
http://social-media0282.s3-website.ap-south-1.amazonaws.com/auth/callback
```

_Note: Add whatever redirect URI path your frontend uses_

## Environment Variable Setup on EC2

Make sure the Google Client ID is set as an environment variable on your EC2 instance:

```bash
# Add to ~/.bashrc or ~/.bash_profile
export GOOGLE_CLIENT_ID="your-google-client-id-here.apps.googleusercontent.com"

# Or pass it when running the JAR
java -jar social-media-0.0.1-SNAPSHOT.jar --GOOGLE_CLIENT_ID="your-client-id"

# Or set it before running
export GOOGLE_CLIENT_ID="your-client-id"
java -jar social-media-0.0.1-SNAPSHOT.jar
```

## Frontend Configuration

Your frontend needs to:

1. Use the correct Google Client ID (same one configured in Google Cloud Console)
2. Send the ID token to: `http://ec2-3-110-55-80.ap-south-1.compute.amazonaws.com:8080/user/api/google-login`
3. Include the ID token in the request body as: `{ "idToken": "..." }`

## Testing OAuth Flow

1. Make sure `GOOGLE_CLIENT_ID` environment variable is set on EC2
2. Restart your Spring Boot application
3. Try logging in with Google from your frontend
4. Check backend logs for any errors

## Common Issues

### "Error: redirect_uri_mismatch"

- The redirect URI used by your frontend doesn't match what's configured in Google Cloud Console
- Add the exact redirect URI to Google Cloud Console

### "Invalid ID token"

- The Client ID in your backend doesn't match the one used in frontend
- Make sure both frontend and backend use the same Google Client ID

### "CORS error"

- Already fixed in your backend configuration
- CORS allows your S3 frontend URL

## Backend API Endpoint

```
POST http://ec2-3-110-55-80.ap-south-1.compute.amazonaws.com:8080/user/api/google-login
Content-Type: application/json

{
  "idToken": "eyJhbGciOiJS..."
}

Response:
{
  "sessionId": "...",
  "expiresAt": 1234567890,
  "userId": 123
}
```
