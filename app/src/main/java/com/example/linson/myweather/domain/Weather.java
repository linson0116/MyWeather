package com.example.linson.myweather.domain;

import java.util.List;

/**
 * Created by linson on 2017/2/13.
 */

public class Weather {
    public List<HeWeather> HeWeather;

    public class HeWeather {
        public Aqi aqi;
        public Basic basic;
        public List<Daily_forecast> daily_forecast;
        public List<Hourly_forecast> hourly_forecast;
        public Now now;
        public String status;
        public Suggestion suggestion;
    }

    public class Aqi {
        public City city;

        public class City {
            public String aqi;
            public String co;
            public String no2;
            public String o3;
            public String pm10;
            public String pm25;
            public String qlty;
            public String so2;
        }
    }

    public class Basic {
        public String city;
        public String cnty;
        public String id;
        public String lat;
        public String lon;
        public Update update;

        public class Update {
            public String loc;
            public String utc;
        }
    }

    public class Daily_forecast {
        public Astro astro;
        public Cond cond;
        public String date;
        public String hum;
        public String pcpn;
        public String pop;
        public String pres;
        public Tmp tmp;
        public String uv;
        public String vis;
        public Wind wind;

        public class Astro {
            public String mr;
            public String ms;
            public String sr;
            public String ss;
        }

        public class Cond {
            public String code_d;
            public String code_n;
            public String txt_d;
            public String txt_n;
        }

        public class Tmp {
            public String max;
            public String min;
        }

        public class Wind {
            public String deg;
            public String dir;
            public String sc;
            public String spd;
        }
    }

    public class Hourly_forecast {
        public Cond cond;
        public String date;
        public String hum;
        public String pop;
        public String pres;
        public String tmp;
        public Wind wind;

        public class Cond {
            public String code;
            public String txt;
        }

        public class Wind {
            public String deg;
            public String dir;
            public String sc;
            public String spd;
        }
    }

    public class Now {
        public Cond cond;
        public String fl;
        public String hum;
        public String pcpn;
        public String pres;
        public String tmp;
        public String vis;
        public Wind wind;

        public class Cond {
            public String code;
            public String txt;
        }

        public class Wind {
            public String deg;
            public String dir;
            public String sc;
            public String spd;
        }

    }

    public class Suggestion {
        public Air air;
        public Comf comf;
        public Cw cw;
        public Drsg drsg;
        public Flu flu;
        public Sport sport;
        public Trav trav;
        public Uv uv;

        public class Air {
            public String brf;
            public String txt;
        }

        public class Comf {
            public String brf;
            public String txt;
        }

        public class Cw {
            public String brf;
            public String txt;
        }

        public class Drsg {
            public String brf;
            public String txt;
        }

        public class Flu {
            public String brf;
            public String txt;
        }

        public class Sport {
            public String brf;
            public String txt;
        }

        public class Trav {
            public String brf;
            public String txt;
        }

        public class Uv {
            public String brf;
            public String txt;
        }
    }
}
