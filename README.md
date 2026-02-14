# JPA1 - Spring Boot Demo Project

This is a demo project for Spring Boot with JPA.

## How to Change Gmail Password

If you need to change your Gmail password (for example, if you're using Gmail SMTP for email functionality), follow these steps:

### Method 1: Change Your Google Account Password

1. **Go to your Google Account**
   - Visit [myaccount.google.com](https://myaccount.google.com)
   - Sign in with your current password if prompted

2. **Navigate to Security Settings**
   - Click on "Security" in the left navigation panel
   - Or go directly to [myaccount.google.com/security](https://myaccount.google.com/security)

3. **Change Your Password**
   - Under "How you sign in to Google", click on "Password"
   - You may need to sign in again for verification
   - Enter your current password
   - Enter your new password (must be at least 8 characters)
   - Re-enter your new password to confirm
   - Click "Change Password"

4. **Update Your Application Settings**
   - After changing your Gmail password, update any applications or services that use your Gmail credentials
   - If using Gmail SMTP in this application, update the `application.properties` file with your new credentials

### Method 2: Use App Passwords (Recommended for Applications)

For applications that need to access your Gmail account, it's recommended to use App Passwords instead of your main Gmail password. This is more secure and allows you to revoke access without changing your main password.

1. **Enable 2-Step Verification** (Required for App Passwords)
   - Go to [myaccount.google.com/security](https://myaccount.google.com/security)
   - Under "How you sign in to Google", click on "2-Step Verification"
   - Follow the prompts to set up 2-Step Verification

2. **Generate an App Password**
   - Go to [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
   - You may need to sign in again
   - Under "Select app", choose "Mail" or "Other (Custom name)"
   - If you choose "Other", enter a name like "Spring Boot App"
   - Under "Select device", choose your device or "Other"
   - Click "Generate"

3. **Copy the App Password**
   - Google will display a 16-character app password
   - Copy this password (you won't be able to see it again)
   - Use this password in your application instead of your regular Gmail password

4. **Use the App Password in Your Application**
   - Update your `application.properties` with the app password:
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-16-character-app-password
   ```

### Security Best Practices

1. **Use Strong Passwords**
   - Use at least 12 characters
   - Include uppercase, lowercase, numbers, and special characters
   - Avoid common words or personal information

2. **Enable 2-Step Verification**
   - Adds an extra layer of security to your account
   - Required for generating App Passwords

3. **Use App Passwords for Applications**
   - More secure than using your main Gmail password
   - Can be revoked individually without affecting other services
   - Easier to manage access for multiple applications

4. **Regular Password Updates**
   - Change your password periodically
   - Update immediately if you suspect unauthorized access

5. **Revoke Unused App Passwords**
   - Go to [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
   - Remove any app passwords you're no longer using

### Troubleshooting

**"Password incorrect" error after changing password:**
- Make sure you've updated the password in all locations where it's used
- Check for extra spaces or characters when copying the password
- If using an app password, ensure you're using the 16-character code without spaces

**"Less secure app access" blocked:**
- Google has disabled "Less secure app access" for all accounts
- You must use App Passwords instead (see Method 2 above)
- Enable 2-Step Verification first, then generate an App Password

**Can't find App Passwords option:**
- You must have 2-Step Verification enabled first
- App Passwords are not available for G Suite accounts with certain security policies
- Contact your G Suite administrator if using a work/school account

## Project Setup

This is a Spring Boot application with JPA support. To run the application:

```bash
./mvnw spring-boot:run
```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run
```

## Requirements

- Java 17 or higher
- Maven (included via Maven Wrapper)

## License

This is a demo project for educational purposes.
