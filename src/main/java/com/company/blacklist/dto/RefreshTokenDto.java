package com.company.blacklist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @project: blacklisting
 */
@Data
@AllArgsConstructor
public class RefreshTokenDto {

    private String token;

    private Date eat;
}
