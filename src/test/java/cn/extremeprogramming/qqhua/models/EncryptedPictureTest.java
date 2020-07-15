package cn.extremeprogramming.qqhua.models;

import deaddrop.Basic;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static java.util.Base64.getMimeDecoder;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EncryptedPictureTest {
    private static final String MESSAGE = "这是我要编码的信息";

    @Test
    public void should_encrypt_message_into_given_file() throws IOException {
        byte[] picture = loadTestPicture("banner.png");
        EncryptedPicture encryptedPicture = new EncryptedPicture(picture, MESSAGE);
        String base64 = encryptedPicture.toBase64();

        byte[] encryptedPictureContent = getMimeDecoder().decode(base64);
        String encryptedPicturePath = "build/tmp/encrypted-banner.png";
        FileOutputStream fileOutputStream = new FileOutputStream(encryptedPicturePath);
        fileOutputStream.write(encryptedPictureContent);
        fileOutputStream.close();

        Basic decoder = new Basic(new String[]{encryptedPicturePath});
        String encryptedData = new String(decoder.decode_data());
        assertThat(encryptedData, is(MESSAGE));
    }

    @Test
    public void should_create_encrypted_file_with_base64() throws IOException {
        String base64 = new String(readAllBytes(get("src/test/resources/encrypted_base64.txt")));
        EncryptedPicture encryptedPicture = new EncryptedPicture(base64);
        assertThat(encryptedPicture.getMessage(), is(MESSAGE));
    }

    @Test
    public void should_create_encrypted_file_with_byte_array() throws IOException {
        byte[] encryptedPictureContent = loadTestPicture("encrypted_picture.png");
        EncryptedPicture encryptedPicture = new EncryptedPicture(encryptedPictureContent);
        assertThat(encryptedPicture.getMessage(), is(MESSAGE));
    }

    @Test
    public void should_demonstrate_usage_in_both_directions() throws IOException {
        byte[] picture = loadTestPicture("banner.png");
        EncryptedPicture encrypted = new EncryptedPicture(picture, MESSAGE);
        String base64 = encrypted.toBase64();

        EncryptedPicture toBeDecrypted = new EncryptedPicture(base64);
        assertThat(toBeDecrypted.getMessage(), is(MESSAGE));
    }

    private byte[] loadTestPicture(String filename) throws IOException {
        return requireNonNull(getClass().getClassLoader().getResourceAsStream(filename)).readAllBytes();
    }
}