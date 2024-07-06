package com.work.media.service;

import com.work.media.dto.VideoFormatDTO;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author linux
 */
public interface VideoService {

    VideoFormatDTO getFormatInfo(String url);

    void downloadVideo(VideoFormatDTO videoInfo, Integer id, OutputStream output) throws IOException;
}
