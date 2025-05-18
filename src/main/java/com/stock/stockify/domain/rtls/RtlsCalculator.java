package com.stock.stockify.domain.rtls;

import java.util.List;

// RSSI -> 거리 변환, 태그 위치 추정 / 핵심 알고리즘
public class RtlsCalculator {

    // RSSI -> 거리 변환
    public static double rssiToDistance(double rssi) {
        double rssiAtOneMeter = -45.0;         // 기준 거리 1m에서의 RSSI
        double pathLossExponent = 2.0;         // 자유 공간 경로 손실 계수
        return Math.pow(10.0, (rssiAtOneMeter - rssi) / (10.0 * pathLossExponent));
    }

    // 태그 위치 추정 (4개 리더기의 좌표 평균 사용)
    public static double[] estimateTagPosition(List<Reader> readers) {
        if (readers.size() < 4) {
            throw new IllegalArgumentException("리더기 최소 4개 필요");
        }

        double sumX = 0, sumY = 0, sumZ = 0;
        for (Reader r : readers) {
            sumX += r.getX();
            sumY += r.getY();
            sumZ += r.getZ();
        }

        int n = readers.size();
        return new double[] { sumX / n, sumY / n, sumZ / n };
    }
}
