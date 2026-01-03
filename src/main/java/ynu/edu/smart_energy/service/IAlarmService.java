package ynu.edu.smart_energy.service;

import ynu.edu.smart_energy.entity.EnergyRecord;
import ynu.edu.smart_energy.entity.MeterDevice;

/**
 * 告警服务：对采集数据做规则检测，并写入告警表
 */
public interface IAlarmService {

    /**
     * 对一条采集记录做告警检测（功率超限、电压异常）
     */
    void checkAndSaveAlarm(MeterDevice device, EnergyRecord record);
}
