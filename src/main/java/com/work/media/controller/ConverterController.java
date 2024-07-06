package com.work.media.controller;

import com.work.media.dto.RequestConverterDTO;
import com.work.media.service.ConverterService;
import com.work.media.util.FileTools;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author linux
 */
@CrossOrigin
@RestController
@RequestMapping("/api/music")
public class ConverterController {

    @Autowired
    private ConverterService service;

    @PostMapping(value = "/converter", consumes = "multipart/form-data", produces = {"audio/mpeg"})
    public void converterMusic(@RequestParam(name = "bitrate", defaultValue = "128000") Integer bitrate,
            @RequestParam(name = "channels", defaultValue = "2") Integer channels,
            @RequestParam(name = "samplingRate", defaultValue = "44100") Integer samplingRate,
            @RequestParam(name = "file") MultipartFile file,
            HttpServletResponse response) throws IOException {
        RequestConverterDTO request = new RequestConverterDTO();
        request.setFile(file);
        request.setSamplingRate(samplingRate);
        request.setChannels(channels);
        request.setBitrate(bitrate);

        String out = FileTools.getExtension(request.getFile().getOriginalFilename());
        response.setContentType("audio/mpeg");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + out + "\"");
        service.converterToMp3(request, response.getOutputStream());
    }
}
