package com.example.shoptest1;

public class Group {
    int id;
    String name;
    String screenName;
    int isClosed;
    String type;
    int isAdmin;
    int isMember;
    int isAdvertiser;
    String photo_50;
    String photo_100;
    String photo_200;

   public Group(int id, String name, String screenName, int isClosed, String type, int isAdmin,
                int isMember, int isAdvertiser, String photo_50, String photo_100, String photo_200) {
       this.id = id;
       this.name = name;
       this.screenName = screenName;
       this.isClosed = isClosed;
       this.type = type;
       this.isAdmin = isAdmin;
       this.isMember = isMember;
       this.isAdvertiser = isAdvertiser;
       this.photo_50 = photo_50;
       this.photo_100 = photo_100;
       this.photo_200 = photo_200;
   }

   public Group() {
       this.id = -1;
       this.name = "";
       this.screenName = "";
       this.isClosed = -1;
       this.type = "";
       this.isAdmin = -1;
       this.isMember = -1;
       this.isAdvertiser = -1;
       this.photo_50 = "";
       this.photo_100 = "";
       this.photo_200 = "";
   }

   public static void toGroup(Group group, String item) {
       group.id = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("id") + 4,
               item.indexOf(",", item.indexOf("id"))));
       group.name = new StringBuilder(item).substring(item.indexOf("name") + 7,
               item.indexOf("\",", item.indexOf("name")));
       group.screenName = new StringBuilder(item).substring(item.indexOf("screen_name") + 14,
               item.indexOf("\",", item.indexOf("screen_name")));
       group.isClosed = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("is_closed") + 11,
               item.indexOf(",", item.indexOf("is_closed"))));
       group.type = new StringBuilder(item).substring(item.indexOf("type") + 7,
               item.indexOf("\",", item.indexOf("type")));
       group.isAdmin = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("is_admin") + 10,
               item.indexOf(",", item.indexOf("is_admin"))));
       group.isAdvertiser = Integer.parseInt(new StringBuilder(item).substring(item.indexOf("is_advertiser") + 15,
               item.indexOf(",", item.indexOf("is_advertiser"))));
       group.photo_50 = new StringBuilder(item).substring(item.indexOf("photo_50") + 11,
               item.indexOf("\",", item.indexOf("photo_50")));
       group.photo_100 = new StringBuilder(item).substring(item.indexOf("photo_100") + 12,
               item.indexOf("\",", item.indexOf("photo_100")));
       group.photo_200 = new StringBuilder(item).substring(item.indexOf("photo_200") + 12,
               item.indexOf("\"}", item.indexOf("photo_200")));
   }
}
