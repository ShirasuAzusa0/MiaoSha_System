package ben.miaoshasystem.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;

import java.util.Base64;

public class RSAKeyUtil {
    // 生成RSA密钥对（2048位）
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }

    // 将生成好的 key 的字节数组写成 Base64 并保存到文件
    public static void writeKeyToFile(Path path, byte[] keyBytes, String header, String footer) throws IOException {
        StringBuilder pem = new StringBuilder();
        pem.append(header).append('\n')
                .append(Base64.getMimeEncoder(64, "\n".getBytes())
                .encodeToString(keyBytes))
                .append('\n')
                .append(footer)
                .append('\n');
        Files.createDirectories(path.getParent());
        Files.write(path, pem.toString().getBytes());
    }

    public static String readKeyFromFile(Path path) throws IOException {
        return Files.readString(path);
    }
}
