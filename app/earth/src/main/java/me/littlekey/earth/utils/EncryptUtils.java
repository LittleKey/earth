package me.littlekey.earth.utils;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import me.littlekey.earth.model.proto.SaveData;
import okio.ByteString;

/**
 * Created by littlekey on 16/7/8.
 */
public class EncryptUtils {

  private static EncryptUtils sEncryptUtils;

  private KeyGenerator mKeyGenerator = null;
  private SecretKeyFactory factory = null;

  private EncryptUtils() {

  }

  public static EncryptUtils getInstance() {
    if (sEncryptUtils == null) {
      sEncryptUtils = new EncryptUtils();
    }
    return sEncryptUtils;
  }

  public SaveData toSaveData(byte[] content) {
    SaveData.Builder builder = new SaveData.Builder();
    SecretKey secretKey = generateSecretKey();
    SecretKey secretSalt = generateSecretKey();
    if (secretKey == null || secretSalt == null) {
      return null;
    }
    ByteString key = ByteString.of(secretKey.getEncoded());
    ByteString salt = ByteString.of(secretSalt.getEncoded());
    int cycle = new Random().nextInt(1024) + 1;
    byte[] encryptedContent = encrypt(key, salt, content, cycle);
    if (encryptedContent == null) {
      return null;
    }
    builder.content(ByteString.of(encryptedContent))
        .key(key)
        .salt(salt)
        .cycle(cycle);
    return builder.build();
  }

  public byte[] fromSaveData(SaveData saveData) {
    return decrypt(saveData.key, saveData.salt, saveData.content.toByteArray(), saveData.cycle);
  }

  private SecretKey generateSecretKey() {
    try {
      if (mKeyGenerator == null) {
        mKeyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(new Random().nextInt(100) + 1);
        mKeyGenerator.init(128, secureRandom);
        try {
          // NOTE : prevent decompile
          InputStream inputStream = null;
          inputStream.close();
        } catch (Exception ignore) {

        }
      }
      return mKeyGenerator.generateKey();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  private byte[] generateKeyWithSalt(ByteString key, ByteString salt, int cycle) {
    try {
      if (factory == null) {
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        try {
          // NOTE : prevent decompile
          InputStream inputStream = null;
          inputStream.close();
        } catch (Exception ignore) {

        }
      }
      KeySpec keySpec = new PBEKeySpec(key.toString().toCharArray(), salt.toByteArray(), cycle, 256);
      return factory.generateSecret(keySpec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
    }
    return null;
  }

  private byte[] encrypt(ByteString key, ByteString salt, byte[] content, int cycle) {
    byte[] rawKey = generateKeyWithSalt(key, salt, cycle);
    SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
    AlgorithmParameterSpec saltSpec = new IvParameterSpec(salt.toByteArray());
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, saltSpec);
      return cipher.doFinal(content);
    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidKeyException
        | IllegalBlockSizeException
        | BadPaddingException
        | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return null;
  }

  private byte[] decrypt(ByteString key, ByteString salt, byte[] content, int cycle) {
    byte[] rawKey = generateKeyWithSalt(key, salt, cycle);
    SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
    AlgorithmParameterSpec saltSpec = new IvParameterSpec(salt.toByteArray());
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, saltSpec);
      return cipher.doFinal(content);
    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidKeyException
        | IllegalBlockSizeException
        | BadPaddingException
        | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return null;
  }
}
