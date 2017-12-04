package com.httpso_hello.hello.Structures;

import java.io.Serializable;

/**
 * Created by mixir on 26.07.2017.
 */

public class Image implements Serializable {

    public String original;
    public String micro;
    public String small;
    public String normal;
    public String big;
    public String normal_form_original;
    public ImageSizes sizes;

    public Image(){

    }
}
