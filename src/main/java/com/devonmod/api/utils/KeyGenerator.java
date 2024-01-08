package com.devonmod.api.utils;

import static com.devonmod.api.exception.Error.ALGORITHM_NOT_AVAILABLE;
import static com.devonmod.api.exception.Error.FILE_CREATION;
import static com.devonmod.api.exception.Error.FILE_WRITE;

import com.devonmod.api.exception.AlgorithmNotAvailableException;
import com.devonmod.api.exception.FileCreationException;
import com.devonmod.api.exception.FileWriteException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.extern.slf4j.Slf4j;

/**
 * The KeyGenerator class provides functionality to generate RSA key pairs and save them as separate
 * public and private key files with the extensions ".der". To create the keys, users can run the
 * main method of this class.
 */
@Slf4j
public class KeyGenerator {

  /**
   * Entry point for generating the RSA key pair and saving them to files. Users can run this method
   * to create the public and private key files.
   */
  public static void main(String[] args) {
    KeyGenerator keyGenerator = new KeyGenerator();

    keyGenerator.generateAndSaveRSAKeyPair();
  }

  /**
   * Generates an RSA key pair consisting of a public key and a private key, and saves them into
   * separate files with the extensions ".der". The public key is saved in the file
   * "src/main/resources/keys/public.der". The private key is saved in the file
   * "src/main/resources/keys/private.der".
   *
   * @throws AlgorithmNotAvailableException If the RSA algorithm is not available on the current
   *                                        platform.
   */
  public void generateAndSaveRSAKeyPair() {
    log.info("Generating RSA key pair...");

    try {
      KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
      keyGenerator.initialize(2048);
      KeyPair kp = keyGenerator.genKeyPair();

      PublicKey publicKey = kp.getPublic();
      PrivateKey privateKey = kp.getPrivate();

      writeToFile("src/main/resources/keys/public.der", publicKey);
      writeToFile("src/main/resources/keys/private.der", privateKey);
      log.info("RSA key pair generated successfully.");
    } catch (NoSuchAlgorithmException e) {
      log.error("RSA algorithm not available", e);
      throw new AlgorithmNotAvailableException(ALGORITHM_NOT_AVAILABLE);
    }
  }

  /**
   * Writes the provided key to a file at the specified path in ".der" format.
   *
   * @param path The file path where the key will be saved.
   * @param key  The key to be written to the file.
   */
  private void writeToFile(String path, Key key) {
    log.info("Writing key to file: {}", path);

    File f = new File(path);

    if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
      log.error("Failed to create parent directory for file: {}", path);
      throw new FileCreationException(FILE_CREATION);
    }

    try (FileOutputStream fos = new FileOutputStream(f)) {
      fos.write(key.getEncoded());
      fos.flush();
      log.info("Key written to file: {}", path);
    } catch (IOException e) {
      log.error("Error writing key to file: {}", path, e);
      throw new FileWriteException(FILE_WRITE);
    }
  }
}
