package com.noobdev.dyel.app;

/**
 * Created by Giovanni on 01/07/2014.
 */
public class Weather {
    public String title;
    public String historia;
    public String qtd;
    public String id;
    public Weather(){
        super();
    }

    public Weather(String title, String Historia, String Qtd, String ID) {
        super();
        this.id = ID;
        this.title = title;
        this.historia = Historia;
        this.qtd = Qtd;
    }
}