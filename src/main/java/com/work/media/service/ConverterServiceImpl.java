package com.work.media.service;

import com.work.media.dto.RequestConverterDTO;
import com.work.media.util.FileTools;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncoderProgressListener;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

/**
 *
 * @author linux
 */
@Service
@Slf4j
public class ConverterServiceImpl implements ConverterService{

    @Autowired
    private Encoder encoder;

    @Override
    public synchronized void converterToMp3(RequestConverterDTO request, OutputStream output) {
        String in = "";
        String out = "";
        try {
            in = request.getFile().getOriginalFilename();
            out = FileTools.getExtension(in);
            File source = new File(in);
            File target = new File(out);
            copyFile(request.getFile().getInputStream(), source);
            log.info("Input: {}", in);
            log.info("Output: {}", out);
            encode(request, source, target);
            copyFile(target, output);
        } catch (IOException ex) {
            log.error("Error: ", ex);
        } finally {
            log.info("\nConverted: [ " + in + " -> " + out + " ] -> 100% \n");
        }
    }

    private void copyFile(InputStream input, File source) throws IOException {
        Files.copy(input, source.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void copyFile(File target, OutputStream output) throws IOException {
        Files.copy(target.toPath(), output);
    }

    private void encode(RequestConverterDTO request, File source, File target) {
        try {
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(request.getBitrate());
            audio.setChannels(request.getChannels());
            audio.setSamplingRate(request.getSamplingRate());

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            ConvertProgressListener listener = new ConvertProgressListener();

            encoder.encode(new MultimediaObject(source), target, attrs, listener);

        } catch (IllegalArgumentException | EncoderException ex) {
            log.error("Error-Encode: {}", ex.getMessage());
        } finally {

        }
    }

    private synchronized void close() {
        if (encoder != null) {
            encoder.abortEncoding();
            log.info("Close");
        }
    }

    private class ConvertProgressListener implements EncoderProgressListener {

        @Override
        public void sourceInfo(MultimediaInfo mi) {
            log.info("Info: {}", mi.getVideo());
        }

        @Override
        public void progress(int p) {
            double progress = p / 1000.00;
            int prog = (int) (progress * 100);

            log.info("{}% ", prog);

            if (prog == 100) {
                close();
            }
        }

        @Override
        public void message(String msg) {

        }
    }
}
