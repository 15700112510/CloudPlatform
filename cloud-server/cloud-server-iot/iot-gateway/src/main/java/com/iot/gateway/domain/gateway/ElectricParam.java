package com.iot.gateway.domain.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/31 15:23
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ElectricParam {
    private long gatewayId;
    private String systemTime;
    private char status;
    private String gridVoltage;
    private String gridCurrent;
    private String gridPower;
    private String gridFrequency;
    private String gridElectricity;
    private char aStatus;
    private String aVoltage;
    private String aCurrent;
    private char bStatus;
    private String bVoltage;
    private String bCurrent;
    private char cStatus;
    private String cVoltage;
    private String cCurrent;
    private char alternatingOneStatus;
    private char alternatingTwoStatus;
    private char alternatingThreeStatus;
    private char directOneStatus;
    private char directTwoStatus;
    private char directThreeStatus;
}
