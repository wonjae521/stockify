package com.stock.stockify.domain.rtls;

// 리더기 위치 및 RSSI 정보 보유 / 데이터 모델
public class Reader {
    private final String id;
    private final double x, y, z;
    private final double rssi;


    public Reader(String id, double x, double y, double z, double rssi) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rssi = rssi;
    }

    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getRssi() { return rssi; }
}