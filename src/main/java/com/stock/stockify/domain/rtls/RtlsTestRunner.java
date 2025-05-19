package com.stock.stockify.domain.rtls;

import java.util.*;

// 기능 단위 테스트 실행용 / 초기 테스트용, 하드코딩
public class RtlsTestRunner {
    public static void main(String[] args) {
        List<Reader> readers = List.of(
                new Reader("R1", 0.0, 0.0, 0.0, -50),
                new Reader("R2", 2.0, 0.0, 0.0, -52),
                new Reader("R3", 0.0, 2.0, 0.0, -49),
                new Reader("R4", 1.0, 1.0, 2.0, -51)
        );

        for (Reader r : readers) {
            double distance = RtlsCalculator.rssiToDistance(r.getRssi());
            System.out.printf("%s → RSSI: %.1f, 거리: %.2fm\n", r.getId(), r.getRssi(), distance); // 추후 정확성 위해 소수점 수정
        }

        double[] pos = RtlsCalculator.estimateTagPosition(readers);
        System.out.printf("\n[추정 위치] x=%.2f, y=%.2f, z=%.2f\n", pos[0], pos[1], pos[2]); // 이하동문
    }
}
