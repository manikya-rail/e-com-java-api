package com.ecommerce.techversantInfotech.Authservice.utils;

import com.ecommerce.techversantInfotech.Authservice.Dto.UserDto;
import com.ecommerce.techversantInfotech.Authservice.Exception.CompletableFutureException;
import com.ecommerce.techversantInfotech.Authservice.Exception.ImageProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageProcessingUtils {

    public static UserDto convertObject(String user) {
        UserDto userDto = new UserDto();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            userDto = objectMapper.readValue(user, UserDto.class);
        } catch (
                IOException err) {
            throw new ImageProcessingException("NOT_PROCESSED", "Image is not processed successfully");
        }
        return userDto;
    }
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }



    public static byte[] decompressImage(byte[] data) {
     Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }
    public static byte[]  compressImageFuture(byte[] imageByte) {
        CompletableFuture<byte[]> compressedImageFuture = CompletableFuture.supplyAsync(() -> compressImage(imageByte));

        try {
            return compressedImageFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CompletableFutureException("ERROR", "Having some trouble to process request");
        }
    }

    public static byte[] decompressImageFuture(byte[] imageBye){
        CompletableFuture<byte[]> decompressedImageFuture=CompletableFuture.supplyAsync(()->decompressImage(imageBye));
        try {
            return decompressedImageFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CompletableFutureException("ERROR", "Having some trouble to process request");
        }
    }
}
