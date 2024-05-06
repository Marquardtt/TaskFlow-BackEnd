package br.demo.backend.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ResizeImage {
    public static byte[] resizeImage(MultipartFile file, int width, int height) throws IOException {
        // Carregar a imagem
        byte[] bytes = file.getBytes();

        // Redimensionar a imagem
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .size(width, height)
                .outputFormat("png")
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
}
