package com.httpso_hello.hello.Structures;

import java.io.Serializable;

/**
 * Created by mixir on 24.08.2017.
 */

public class FlirtikItem implements Serializable {

    public int id;
    public String nickname;
    public Image avatar;
    public String birth_date;
    public String city_cache;

    public FlirtikItem(){

    }
}
