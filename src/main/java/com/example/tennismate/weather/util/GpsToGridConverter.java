package com.example.tennismate.weather.util;

public class GpsToGridConverter {

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //둥근 지구의 위도/경도를 평평한 2차원으로 생각해 격자 x,y로변환하는 (지도투영)
    public static Point convert(double latitude, double longitude) {

        //기상청 지도를 만들기위해 설정한 기본값
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        int XO = 43; // 기준점 X 격자 좌표
        int YO = 136; // 기준점 Y 격자 좌표


        double DEGRAD = Math.PI / 180.0; //각도를 라디언 값으로 바꿈
        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + latitude * DEGRAD * 0.5);   //북극점으로 부터 현재 위도까지의 거리
        ra = re * sf / Math.pow(ra, sn);
        double theta = longitude * DEGRAD - olon;   //기준경도로부터 현재 경도까지 각도
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        //삼각함수를 이용해 거리와 각도를 직교 좌표로 바꿈
        int x = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int y = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return new Point(x, y);
    }
}
