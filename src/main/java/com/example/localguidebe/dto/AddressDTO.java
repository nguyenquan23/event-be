package com.example.localguidebe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDTO {
    private String road;
    private String quarter;
    private String suburb;
    private String cityDistrict;
    private String city;
    private String ISO3166_2_lvl4;
    private String postcode;
    private String country;
    private String countryCode;

}
