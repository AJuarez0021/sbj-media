package com.work.media.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author linux
 */
@Data
public class RequestConverterDTO implements java.io.Serializable{

    private MultipartFile file;
    private Integer bitrate;
    private Integer channels;
    private Integer samplingRate;
    
        
}
