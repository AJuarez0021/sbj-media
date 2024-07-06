package com.work.media.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author linux
 */
@Data
@Builder
public class VideoFormatDTO {
    private String url;    
    private String title;
    private Boolean downloadable;
    private long viewCount;
    private String description;
    private String time;    
    private String thumbnail;
    private List<FormatDTO> audio;
    private List<FormatDTO> video;
    
    
}
