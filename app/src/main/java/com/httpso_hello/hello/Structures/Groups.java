package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 24.11.2017.
 */

public class Groups {

    public int id;
    public int ownerId;
    public String title;
    public String description;
    public Image logo;
    public int membersCount;
    public boolean moderate;
    public int count_of_moderate;

    public cmsGroupsMembers cms_groups_members;

    public boolean isSubscribed;

    Groups() {

    }

}
