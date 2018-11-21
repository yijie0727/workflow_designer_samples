package cz.zcu.kiv.eeg.basil.data;

/**
 * Base EEG message object
 *
 * Created by Tomas Prokop on 17.07.2017.
 */
public class EEGMessage {

    /**
     * Unique message ID
     */
    private final int messageId;

    /**
     * Message
     */
    private String message;

    /**
     * Default constructor
     * @param meaageId unique message ID
     */
    public EEGMessage(int meaageId) {
        this.messageId = meaageId;
    }

    /**
     * Get message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message
     * @param message Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get message ID
     * @return ID
     */
    public int getMessageId() {
        return messageId;
    }
}
