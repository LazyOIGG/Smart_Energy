create table building
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    name          varchar(64)                        not null comment '建筑名称（如：力行楼、楸苑宿舍三号楼）',
    location_code varchar(64)                        null comment '位置编号（可自定义，如：BLD01）',
    floors        int      default 1                 null comment '楼层数',
    usage_type    varchar(64)                        null comment '建筑用途分类（宿舍/教学楼/实验楼/图书馆等）',
    created_at    datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '建筑区域信息表' charset = utf8mb4;

create table meter_device
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    name        varchar(64)                           not null comment '设备名称（如：宿舍智能电表-01）',
    sn          varchar(64)                           not null comment '唯一设备序列号SN（硬件唯一标识，如：METER_QIU_301）',
    status      varchar(16) default 'ONLINE'          not null comment '通讯状态：ONLINE在线 / OFFLINE离线',
    pmax        double                                not null comment '额定功率阈值Pmax（W）',
    building_id bigint                                not null comment '外键_所属建筑ID',
    room_no     varchar(32)                           not null comment '外键_所属房间号（如：301、101）',
    created_at  datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    active      tinyint(1)  default 1                 not null comment '是否有效：1有效（正在使用）/0停用或拆除',
    constraint uk_building_room_active
        unique (building_id, room_no, active),
    constraint uk_device_sn
        unique (sn),
    constraint fk_device_building
        foreign key (building_id) references building (id)
            on update cascade
)
    comment '智能电表设备信息表' charset = utf8mb4;

create table alarm_record
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    device_id    bigint       not null comment '外键_所属设备ID',
    alarm_type   varchar(32)  not null comment '告警类型：OVERLOAD功率超限 / VOLTAGE_ABNORMAL电压异常',
    alarm_value  double       not null comment '告警数值（功率W 或 电压V）',
    detail       varchar(255) null comment '告警详情描述（如：功率1200W超过阈值1000W）',
    triggered_at datetime     not null comment '触发时间',
    constraint fk_alarm_device
        foreign key (device_id) references meter_device (id)
            on update cascade
)
    comment '异常告警记录表' charset = utf8mb4;

create index idx_alarm_device_time
    on alarm_record (device_id, triggered_at);

create index idx_alarm_time
    on alarm_record (triggered_at);

create table energy_record
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    device_id    bigint   not null comment '外键_所属设备ID',
    voltage      double   not null comment '当前电压(V)',
    current      double   not null comment '当前电流(A)',
    power        double   not null comment '当前实时功率(W)（业务层保证 P=U×I）',
    total_kwh    double   not null comment '累计用电量(kWh)',
    collected_at datetime not null comment '采集时间戳',
    constraint fk_record_device
        foreign key (device_id) references meter_device (id)
            on update cascade
)
    comment '能耗采集记录表（模拟采集数据）' charset = utf8mb4;

create index idx_record_device_time
    on energy_record (device_id, collected_at);

create index idx_building_room_active
    on meter_device (building_id, room_no, active);

create table sys_user
(
    id         bigint auto_increment comment '主键ID'
        primary key,
    username   varchar(64)                          not null comment '用户名（唯一）',
    password   varchar(255)                         not null comment '密码（BCrypt加密）',
    role       varchar(32)                          not null comment '角色：ADMIN管理员 / USER普通用户',
    enabled    tinyint(1) default 1                 not null comment '是否启用：1启用 0禁用',
    created_at datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    constraint username
        unique (username)
)
    comment '系统用户表' charset = utf8mb4;

