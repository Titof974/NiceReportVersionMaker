import reportversion.Report;
import utils.CryptoUtils;
import utils.FileUtils;
import utils.FileUtils.*;
import utils.HashUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class main {
    public static void main(String[] args){
        Report report = new Report("test1", ".", ".*class", ".*main.*");
        try {
            report.generate();
            report.checkFiles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(CryptoUtils.crypt(report.toJson()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            Report.fromJson(report.cryptedJson());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String jsonReport = null;
//        try {
//            jsonReport = CryptoUtils.decrypt("");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Report report = Report.fromJson(jsonReport);
//        try {
//            report.checkFiles();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        System.out.println(report);

//        try {
//            String crypt = CryptoUtils.crypt("salut");
//            System.out.println(crypt);
//            String decrypt = CryptoUtils.decrypt(crypt);
//            System.out.println(decrypt);
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //        try {
//            FileUtils.listAllInDirectory(".",
//                    Optional.of(".*\\\\classes\\\\.*"),
//                    Optional.of(".*\\.java$"))
//                    .stream()
//                    .peek(System.out::println)
//                    .map(p -> {
//                        try {
//                            return HashUtils.generateSha256(p);
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return "";
//                    })
//                    .forEach(System.out::println);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
