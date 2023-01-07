package com.screenscraper.mapper;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDTO {
//    private String jurisdiction;
    private String fileNumber;
    private String saleDate;
    private String saleTime;
    private String propertyAddress;
    private String city;
    private String zip;
    private String originalLoanAmount;

    @Override
    public String toString() {
        return "RecordDTO{" +
//                "jurisdiction='" + jurisdiction + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                ", saleDate='" + saleDate + '\'' +
                ", saleTime='" + saleTime + '\'' +
                ", propertyAddress='" + propertyAddress + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", originalLoanAmount='" + originalLoanAmount + '\'' +
                '}';
    }
}
