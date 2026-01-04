package ynu.edu.smart_energy.service;

import ynu.edu.smart_energy.dto.stats.DevicePowerPointDTO;

import java.util.List;

public interface IStatsService {

    /**
     * 折线图：某设备最近10次采集功率趋势
     */
    List<DevicePowerPointDTO> getRecent10PowerTrend(Long deviceId);
}
