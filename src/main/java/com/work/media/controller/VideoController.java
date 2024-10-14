package com.work.media.controller;

import com.work.media.dto.VideoFormatDTO;
import com.work.media.service.VideoService;
import com.work.media.util.FileTools;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author linux
 */
@CrossOrigin
@RestController
@RequestMapping("/api/video")
@Slf4j
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping(path = "/formats", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public VideoFormatDTO getFormats(@RequestParam("url") String url) {
        return  videoService.getFormatInfo(url);        
    }

    @GetMapping(path = "/download", produces = {"video/mp4"})
    @ResponseBody
    public void downloadVideo(@RequestParam("url") String url,
            @RequestParam("id") Integer id,
            HttpServletResponse response) throws IOException {
        VideoFormatDTO videoInfo = videoService.getFormatInfo(url);
        String out = FileTools.getExtension(videoInfo.getTitle(), "mp4");                
        out = URLEncoder.encode(out, StandardCharsets.UTF_8);
        log.info("FileName: {}", out);
        response.setContentType("video/mp4");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + out + "\"");
        videoService.downloadVideo(videoInfo, id, response.getOutputStream());
    }
}
