# Secure-File-Management-System
A Secure File Management System (SFMS) in Java that offers authentication, encryption, access control, and threat detection.   Includes file operations (create, read, write, share), encryption/decryption, and security threat detection (buffer overflow &amp; malware patterns). 


Develop a secure file management system that incorporates authentication mechanisms (password-based, two-factor):

The Authentication & Access Control Module is responsible for verifying the user's identity and managing their access rights. It ensures that only authorized users can interact with the Secure File Management System (SFMS), adding an extra layer of security.
1. Username-Password Authentication:
   Users log in by providing their username and password.
   The system verifies the credentials against the stored values.
   If the username and password match → Authentication is successful.
   If they do not match → Access is denied.
2. Two-Factor Authentication (2FA):
   After the initial authentication, users undergo 2FA verification.
   The system generates a random 6-digit One-Time Password (OTP).
   The user must enter the correct OTP to gain access.
   If the OTP is incorrect → Access is denied.
   This ensures extra security, even if the username-password is compromised.
3. Role-Based Access Control (RBAC):
   The system supports two roles:
   Admin → Full access (including sensitive operations).
   User → Limited access (basic operations only).
