package com.hjx.netty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class FileChannel {
    private static Logger logger = Logger.getLogger("FileChannel");
    public static void main(String[] args) {
        logger.info("开始使用Java NIO");
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\healthcheck_cfg_1.txt","rw");
            java.nio.channels.FileChannel fileChannel = randomAccessFile.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            int bytesRead = fileChannel.read(byteBuffer);
            while (bytesRead!=-1){
                byteBuffer.flip();

                while(byteBuffer.hasRemaining()){
                    System.out.print((char) byteBuffer.get()); // read 1 byte at a time
                }
                byteBuffer.clear(); //make buffer ready for writing
                bytesRead = fileChannel.read(byteBuffer);
            }

            fileChannel.close();
        }catch (FileNotFoundException e){
            logger.info("找不到文件");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }
}
