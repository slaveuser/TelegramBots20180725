package org.telegram.telegrambots.meta.api.methods.send;

import com.fasterxml.jackson.core.type.TypeReference;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Use this method to send voice notes, if you want Telegram clients to display the file as a
 * playable voice message. For this to work, your audio must be in an .ogg file encoded with OPUS
 * (other formats may be sent as Audio or Document).
 */
public class SendVoice extends PartialBotApiMethod<Message> {
    public static final String PATH = "sendvoice";

    public static final String CHATID_FIELD = "chat_id";
    public static final String VOICE_FIELD = "voice";
    public static final String DISABLENOTIFICATION_FIELD = "disable_notification";
    public static final String REPLYTOMESSAGEID_FIELD = "reply_to_message_id";
    public static final String REPLYMARKUP_FIELD = "reply_markup";
    public static final String DURATION_FIELD = "duration";
    public static final String CAPTION_FIELD = "caption";
    public static final String PARSEMODE_FIELD = "parse_mode";

    private String chatId; ///< Unique identifier for the chat sent message to (Or username for channels)
    private String voice; ///< Audio file to send. You can either pass a file_id as String to resend an audio that is already on the Telegram servers, or upload a new audio file using multipart/form-data.
    private Boolean disableNotification; ///< Optional. Sends the message silently. Users will receive a notification with no sound.
    private Integer replyToMessageId; ///< Optional. If the message is a reply, ID of the original message
    private ReplyKeyboard replyMarkup; ///< Optional. JSON-serialized object for a custom reply keyboard
    private Integer duration; ///< Optional. Duration of sent audio in seconds
    private String caption; ///< Optional. Voice caption (may also be used when resending videos by file_id).
    private String parseMode; ///< Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.

    private boolean isNewVoice; ///< True to upload a new voice note, false to use a fileId
    private String voiceName; ///< Name of the voice note
    private File newVoiceFile; ///< New voice note file
    private InputStream newVoiceStream; ///< New voice note stream

    public SendVoice() {
        super();
    }

    public Boolean getDisableNotification() {
        return disableNotification;
    }

    public SendVoice enableNotification() {
        this.disableNotification = false;
        return this;
    }

    public SendVoice disableNotification() {
        this.disableNotification = true;
        return this;
    }

    public String getChatId() {
        return chatId;
    }

    public SendVoice setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public SendVoice setChatId(Long chatId) {
        Objects.requireNonNull(chatId);
        this.chatId = chatId.toString();
        return this;
    }

    public String getVoice() {
        return voice;
    }

    public SendVoice setVoice(String voice) {
        this.voice = voice;
        this.isNewVoice = false;
        return this;
    }

    public SendVoice setNewVoice(File file) {
        this.isNewVoice = true;
        this.newVoiceFile = file;
        return this;
    }

    public SendVoice setNewVoice(String voiceName, InputStream inputStream) {
    	Objects.requireNonNull(voiceName, "voiceName cannot be null!");
    	Objects.requireNonNull(inputStream, "inputStream cannot be null!");
    	this.voiceName = voiceName;
        this.isNewVoice = true;
        this.newVoiceStream = inputStream;
        return this;
    }

    public Integer getReplyToMessageId() {
        return replyToMessageId;
    }

    public SendVoice setReplyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    public ReplyKeyboard getReplyMarkup() {
        return replyMarkup;
    }

    public SendVoice setReplyMarkup(ReplyKeyboard replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public SendVoice setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public boolean isNewVoice() {
        return isNewVoice;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public File getNewVoiceFile() {
        return newVoiceFile;
    }

    public InputStream getNewVoiceStream() {
        return newVoiceStream;
    }

    public String getCaption() {
        return caption;
    }

    public SendVoice setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getParseMode() {
        return parseMode;
    }

    public SendVoice setParseMode(String parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    @Override
    public Message deserializeResponse(String answer) throws TelegramApiRequestException {
        try {
            ApiResponse<Message> result = OBJECT_MAPPER.readValue(answer,
                    new TypeReference<ApiResponse<Message>>(){});
            if (result.getOk()) {
                return result.getResult();
            } else {
                throw new TelegramApiRequestException("Error sending voice", result);
            }
        } catch (IOException e) {
            throw new TelegramApiRequestException("Unable to deserialize response", e);
        }
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null) {
            throw new TelegramApiValidationException("ChatId parameter can't be empty", this);
        }

        if (isNewVoice) {
            if (newVoiceFile == null && newVoiceStream == null) {
                throw new TelegramApiValidationException("Voice can't be empty", this);
            }
            if (newVoiceStream != null && (voiceName == null || voiceName.isEmpty())) {
                throw new TelegramApiValidationException("Voice name can't be empty", this);
            }
        } else if (voice == null) {
            throw new TelegramApiValidationException("Voice can't be empty", this);
        }

        if (replyMarkup != null) {
            replyMarkup.validate();
        }
    }

    @Override
    public String toString() {
        return "SendVoice{" +
                "chatId='" + chatId + '\'' +
                ", voice='" + voice + '\'' +
                ", disableNotification=" + disableNotification +
                ", replyToMessageId=" + replyToMessageId +
                ", replyMarkup=" + replyMarkup +
                ", duration=" + duration +
                ", caption='" + caption + '\'' +
                ", parseMode='" + parseMode + '\'' +
                ", isNewVoice=" + isNewVoice +
                ", voiceName='" + voiceName + '\'' +
                ", newVoiceFile=" + newVoiceFile +
                ", newVoiceStream=" + newVoiceStream +
                '}';
    }
}
