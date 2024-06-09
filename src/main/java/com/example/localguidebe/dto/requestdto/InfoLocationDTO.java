package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.dto.AddressDTO;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InfoLocationDTO {
    private long placeId;
    private String licence;
    private String osmType;
    private long osmId;
    private String lat;
    private String lon;
    private String category;
    private String type;
    private int placeRank;
    private double importance;
    private String addressType;
    private String name;
    private String display_name;
    private AddressDTO address;
    private List<String> boundingbox;
}
