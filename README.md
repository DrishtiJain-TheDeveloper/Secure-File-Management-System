# Secure-File-Management-System
A Secure File Management System (SFMS) in Java that offers authentication, encryption, access control, and threat detection. Includes file operations (create, read, write, share), encryption/decryption, and security threat detection (buffer overflow & malware patterns). 

The Secure File Management System (SFMS) is a comprehensive Java-based application that ensures high levels of security for users managing sensitive files. The system integrates several key features like authentication, encryption, access control, and threat detection to protect data from unauthorized access, manipulation, and malicious attacks.

Key Features:
Authentication: Ensures only authorized users can access the system.

Encryption: Safeguards files and their contents with encryption and decryption processes.

Access Control: Manages user roles and their permissions for interacting with files.

Threat Detection: Monitors potential threats like malware and buffer overflow patterns.

1. Authentication & Access Control
The Authentication & Access Control Module verifies the identity of the users and manages what actions they are allowed to perform. This module has three main components:

a. Username-Password Authentication:
The system starts by verifying the user’s identity based on a username and password. The steps are as follows:

User Login:

The user is prompted to enter a username and password.

The system matches the entered credentials against a stored database (e.g., a hashed password in a secure storage location).

If the credentials are correct, the system proceeds with the authentication.

If they are incorrect, access is denied, and the user is given an error message.

b. Two-Factor Authentication (2FA):
To enhance security, the system incorporates Two-Factor Authentication (2FA). This is an additional step after the username-password verification. The system generates a random One-Time Password (OTP), which the user must input. The procedure works as follows:

OTP Generation:

After the user’s initial login with a correct username and password, the system generates a random OTP (a 6-digit number) and sends it to the user (via email or SMS).

OTP Input:

The user is prompted to enter the OTP.

The system checks if the entered OTP matches the generated one. If they match, access is granted.

If the OTP is incorrect or expired, access is denied.

This adds an additional layer of security, ensuring that even if the username/password combination is compromised, the system remains protected.

c. Role-Based Access Control (RBAC):
RBAC is implemented to control the level of access granted to each user based on their role. The system defines two roles:

Admin:

Full access to all system features, including file operations, encryption settings, user management, and threat monitoring.

Admin can also modify security settings, user permissions, and system configurations.

User:

Limited access, only able to perform basic file operations such as creating, reading, and writing files.

Users cannot access sensitive data or alter system security settings.

2. File Operations
The system supports a variety of file management operations, ensuring that files are stored securely while maintaining user accessibility. These operations include:

Create: Users can create new files, ensuring the file is stored securely within the system.

Read: Users can view the contents of files, but only if they have the appropriate permissions based on their role.

Write: Users can modify the contents of files they have access to.

Share: Users can securely share files with others, but only if authorized (Admin or User with necessary permissions).

3. Encryption/Decryption
To safeguard sensitive file data, the system uses encryption algorithms to protect files. When a user uploads or creates a file, it is automatically encrypted before being stored. Decryption happens when the user needs to access or edit the file. Here’s how it works:

Encryption:

When a file is uploaded or created by the user, it is encrypted using a secure encryption algorithm (e.g., AES-256).

The encryption key is stored securely and is never exposed.

Encrypted files are stored in a separate location from their original form.

Decryption:

When a user attempts to open or modify an encrypted file, the system uses the decryption key to decrypt the file.

Only authorized users (based on their role) can decrypt the files they have access to.

4. Threat Detection
The SFMS incorporates a threat detection system that actively monitors potential security vulnerabilities and attacks. Some key features include:

a. Malware Detection:
The system includes malware detection techniques to identify files that may contain harmful software (e.g., viruses, trojans, or ransomware). The detection system scans uploaded files for known signatures of malicious code and alerts the user or administrator if malware is detected.

b. Buffer Overflow Protection:
Buffer overflow vulnerabilities are a common security threat in many systems. The SFMS includes mechanisms to detect and prevent buffer overflow attacks that could allow attackers to manipulate the system or gain unauthorized access.

The system uses input validation checks to ensure that users cannot overload buffers by providing excessively large inputs that could compromise security.

Any suspicious behavior is logged, and users are notified of potential risks.

5. File Sharing & Permissions
The system provides secure file-sharing capabilities, allowing users to share files while maintaining full control over their data. Administrators can manage file access rights, setting specific permissions for users or groups:

Read-Only Access: Users can view the file but cannot modify it.

Read-Write Access: Users can both view and edit the file.

Admin-Only Access: Files restricted to administrators only, ensuring that sensitive data is protected.

