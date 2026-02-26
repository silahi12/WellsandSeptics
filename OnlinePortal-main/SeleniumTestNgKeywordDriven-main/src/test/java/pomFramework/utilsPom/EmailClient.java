package pomFramework.utilsPom;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailClient {
private String host  = "imap.gmail.com";
private String username;
private String password; // IMPORTANT: Use App Password for Gmail/Outlook if 2FA is on
private static final String senderEmail = "noreply@maryland.gov";
private static final String otpSubject ="Online Portal Security Code";
private int timeoutSeconds;

private Store store;
private Folder inbox;

/**
 * Initializes the EmailClient with email account details and OTP criteria.
 *
 * //@param host Email server IMAP host (e.g., "imap.gmail.com", "outlook.office365.com").
 * @param username Your email address.
 * @param password Your email password or App Password (HIGHLY RECOMMENDED).
 * //@param senderEmail The expected sender's email address for the OTP.
 * //@param otpSubject The expected subject line of the OTP email.
 * @param timeoutSeconds The maximum time to wait for the OTP email.
 */
//public EmailClient(String host, String username, String password, String senderEmail, String otpSubject, int timeoutSeconds)
public EmailClient(String username, String password,int timeoutSeconds) {
    //this.host = host;
    this.username = username;
    this.password = password;
    //this.senderEmail = senderEmail;
    //this.otpSubject = otpSubject;
    this.timeoutSeconds = timeoutSeconds;
}

/**
 * Connects to the email inbox.
 *
 * @throws MessagingException if unable to connect or open inbox.
 */
public void connect() throws MessagingException {
    Properties properties = new Properties();
    properties.put("mail.store.protocol", "imaps"); // Use imaps for secure IMAP
    properties.put("mail.imaps.host", host);
    properties.put("mail.imaps.port", "993"); // Standard IMAPS port
    properties.put("mail.imaps.ssl.enable", "true"); // Enable SSL for IMAPS

    Session emailSession = Session.getDefaultInstance(properties);
    store = emailSession.getStore("imaps");
    store.connect(host, username, password);
    inbox = store.getFolder("INBOX");
    inbox.open(Folder.READ_WRITE); // Open in read-write mode to allow marking messages as read/deleted
    System.out.println("Connected to email inbox: " + username);
}

/**
 * Closes the email inbox connection.
 */
public void disconnect() {
    try {
        if (inbox != null && inbox.isOpen()) {
            inbox.close(false); // Close folder without expunging (deleting marked messages)
        }
        if (store != null && store.isConnected()) {
            store.close();
        }
        System.out.println("Disconnected from email inbox.");
    } catch (MessagingException e) {
        System.err.println("Error disconnecting from email: " + e.getMessage());
    }
}

/**
 * Fetches and parses the latest OTP from the inbox.
 * It waits for the email to arrive within a specified timeout.
 *
 * @return The extracted OTP as a String, or null if not found within the timeout.
 */
public String getLatestOtp() {
    String otp = null;
    Instant startTime = Instant.now();
    Instant endTime = startTime.plusSeconds(timeoutSeconds);

    System.out.println("Waiting for OTP email (from: " + senderEmail + ", subject: '" + otpSubject + "')...");

    while (Instant.now().isBefore(endTime) && otp == null) {
        try {
            // Search for new messages
            Message[] messages = searchForOtpEmail(senderEmail, otpSubject);

            if (messages.length > 0) {
                // Sort messages by date received (latest first)
                // Note: JavaMail doesn't guarantee order, so sorting by ReceivedDate is good.
                Arrays.sort(messages, Comparator.comparing(m -> {
                                       try {
                                               return m.getReceivedDate();
                                          } catch (MessagingException e) { return new Date(0); }
                                    }));
                Message latestMessage = messages[messages.length - 1];

                // Ensure the message is recent (e.g., within the last 25 seconds)
                if (latestMessage.getReceivedDate().toInstant().isAfter(Instant.now().minus(Duration.ofSeconds(25)))) {
                    otp = parseOtpFromMessage(latestMessage);
                    if (otp != null) {
                        System.out.println("OTP found: " + otp);
                    }
                } else {
                    System.out.println("Found older message, but not recent enough. Waiting for new OTP email.");
                }
            }

            if (otp == null) {
                // Wait a bit before checking again
                Thread.sleep(2000); // Check every 2 seconds
                inbox.expunge(); // Clear deleted messages and update folder status
                inbox.close(false); // Reopen to get fresh message list
                inbox.open(Folder.READ_WRITE);
            }

        } catch (MessagingException | IOException | InterruptedException e) {
            System.err.println("Error while fetching/parsing OTP email: " + e.getMessage());
            // Don't re-throw, just log and continue waiting or return null
        }
    }
    return otp;
}


/**
 * Searches the inbox for messages matching sender and subject from the last few minutes.
 *
 * @param senderEmail The expected sender's email.
 * @param otpSubject The expected subject of the email.
 * @return An array of matching messages.
 * @throws MessagingException
 */
private Message[] searchForOtpEmail(String senderEmail, String otpSubject) throws MessagingException {
    // Define search terms
    Address senderAddress = new InternetAddress(senderEmail);
    SearchTerm fromTerm = new FromTerm(senderAddress);
    //SearchTerm fromTerm = new FromStringTerm(senderEmail);
    SearchTerm subjectTerm = new SubjectTerm(otpSubject);
    // Search for messages received in the last 10 minutes (adjust as needed)
    SearchTerm receivedDateTerm = new ReceivedDateTerm(ReceivedDateTerm.GT, new Date(System.currentTimeMillis() - Duration.ofMinutes(10).toMillis()));

    //SearchTerm searchTerm = new AndTerm(fromTerm, new AndTerm(subjectTerm, receivedDateTerm));
    SearchTerm searchTerm = new AndTerm(fromTerm, subjectTerm);

    return inbox.search(searchTerm);
}

/**
 * Parses the OTP from the content of an email message.
 * This method needs to be customized based on the actual format of your OTP email.
 *
 * @param message The email message to parse.
 * @return The extracted OTP string, or null if not found.
 * @throws MessagingException
 * @throws IOException
 */
private String parseOtpFromMessage(Message message) throws MessagingException, IOException {
    Object content = message.getContent();
    String emailContent = "";

    if (content instanceof String) {
        emailContent = (String) content;
    } else if (content instanceof MimeMultipart) {
        MimeMultipart mimeMultipart = (MimeMultipart) content;
        emailContent = getTextFromMimeMultipart(mimeMultipart);
    }

    // Define a regex pattern to extract the OTP.
    // This is a common pattern for 4-6 digit OTPs. Adjust if your OTP format is different.
    // Example: "Your OTP is 123456" -> Pattern: "Your OTP is (\d{6})"
    // Example: "OTP: 12345" -> Pattern: "OTP: (\d{5})"
    //Pattern otpPattern = Pattern.compile("(?<!\\d)(\\d{4}|\\d{5}|\\d{6})(?!\\d)"); // Matches 4, 5, or 6 digit numbers not preceded/followed by other digits

    // Or more specific if the OTP is always like "Your security code: 123456"
    Pattern otpPattern = Pattern.compile("Your security code:.*?(\\d{6})");


    Matcher matcher = otpPattern.matcher(emailContent);
    if (matcher.find()) {
        System.out.println("Email Content: " + emailContent); // Print email content for debugging
        return matcher.group(1); // Return the first capturing group (the OTP itself)
    }
    return null;
}

/**
 * Extracts plain text content from a MimeMultipart email.
 * @param mimeMultipart The MimeMultipart object.
 * @return The plain text content.
 * @throws MessagingException
 * @throws IOException
 */
private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
    StringBuilder result = new StringBuilder();
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
        if (bodyPart.isMimeType("text/plain")) {
            result.append(bodyPart.getContent());
            break; // stop once plain text is found
        } else if (bodyPart.isMimeType("text/html")) {
            // Can also extract from HTML if plain text is not available
            // For OTP, plain text is usually sufficient and simpler.
            result.append(bodyPart.getContent());
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
        }
    }
    return result.toString();
}

    /**
     * Retrieve latest OTP
     */
    public String retrieveLatestOtp() {
        String otp = "";
        try {
            connect(); // The connect method uses the instance variables
            otp = getLatestOtp();
            if (otp != null) {
                System.out.println("Retrieved OTP: " + otp);
            } else {
                System.out.println("OTP not received within timeout.");
            }
        } catch (MessagingException e) {
            System.err.println("Failed to connect or fetch email: " + e.getMessage());
        } finally {
            disconnect();
        }
        return otp;
    }

public static void main(String[] args) {
    // --- IMPORTANT: REPLACE WITH YOUR ACTUAL EMAIL CONFIG AND APP PASSWORD ---
    // For Gmail, enable IMAP in settings and generate an App password:
    // Google Account -> Security -> 2-Step Verification -> App passwords
    //String host = "imap.gmail.com";
    String username = "a2zvitamin14@gmail.com"; // Your email
    String password = "gdww djbi hfvl khbw"; // Your App Password or actual password (if no 2FA)
    //String senderEmail = "noreply@maryland.gov"; // Sender of the OTP email
    //String otpSubject = "Online Portal Security Code"; // Subject of the OTP email

    EmailClient emailClient = new EmailClient(username, password,  60); // 60 seconds timeout

    try {
        emailClient.connect();
        String otp = emailClient.getLatestOtp();
        if (otp != null) {
            System.out.println("Retrieved OTP: " + otp);
        } else {
            System.out.println("OTP not received within timeout.");
        }
    } catch (MessagingException e) {
        System.err.println("Failed to connect or fetch email: " + e.getMessage());
    } finally {
        emailClient.disconnect();
    }
}
}

