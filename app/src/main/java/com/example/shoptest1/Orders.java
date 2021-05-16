package com.example.shoptest1;

public class Orders {
    String id;
    String userId;
    String date;
    String status;
    String itemsCount;
    String totalPriceText;
    String displayOrderId;
    String trackNumber;
    String address;
    String description;
    String title;
    String thumbPhoto;
    String sellerTitle;
    String sellerName;
    String sellerGroupId;

    public Orders() {
        this.id = "-1";
        this.userId = "-1";
        this.date = "-1";
        this.status = "-1";
        this.itemsCount = "-1";
        this.totalPriceText = "-1";
        this.displayOrderId = "-1";
        this.trackNumber = "-1";
        this.address = "-1";
        this.description = "-1";
        this.thumbPhoto = "-1";
        this.title = "-1";
        this.sellerTitle = "-1";
        this.sellerName = "-1";
        this.sellerGroupId = "-1";
    }

    public static void toOrder(Orders order, String item) {
        order.id = new StringBuilder(item).substring(item.indexOf("id") + 4, item.indexOf(",", item.indexOf("id")));
        order.userId = new StringBuilder(item).substring(item.indexOf("user_id") + 9, item.indexOf(",", item.indexOf("user_id")));
        order.date = new StringBuilder(item).substring(item.indexOf("date") + 6, item.indexOf(",", item.indexOf("date")));
        order.status = new StringBuilder(item).substring(item.indexOf("status") + 8, item.indexOf(",", item.indexOf("status")));
        order.itemsCount = new StringBuilder(item).substring(item.indexOf("items_count") + 13, item.indexOf(",", item.indexOf("items_count")));
        order.totalPriceText = new StringBuilder(item).substring(item.indexOf("text", item.indexOf("total_price")) + 7,
                item.indexOf("\"", item.indexOf("text", item.indexOf("total_price")) + 7));
        order.displayOrderId = new StringBuilder(item).substring(item.indexOf("display_order_id") + 19, item.indexOf("\"", item.indexOf("display_order_id") + 19));
        order.trackNumber = new StringBuilder(item).substring(item.indexOf("track_number") + 15, item.indexOf("\"", item.indexOf("track_number") + 15));
        order.address = new StringBuilder(item).substring(item.indexOf("address") + 10, item.indexOf("\"", item.indexOf("address") + 10));
        order.description = new StringBuilder(item).substring(item.indexOf("description") + 14, item.indexOf("\"", item.indexOf("description") + 14));
        order.thumbPhoto = new StringBuilder(item).substring(item.indexOf("thumb_photo") + 14, item.indexOf("\"", item.indexOf("thumb_photo") + 14));
        order.title = new StringBuilder(item).substring(item.indexOf("title", item.indexOf("thumb_photo")) + 8,
                item.indexOf("\"", item.indexOf("title", item.indexOf("thumb_photo")) + 8));
        order.sellerTitle = new StringBuilder(item).substring(item.indexOf("title", item.indexOf("seller")) + 8,
                item.indexOf("\"", item.indexOf("title", item.indexOf("seller")) + 8));
        order.sellerName = new StringBuilder(item).substring(item.indexOf("name", item.indexOf("seller")) + 7,
                item.indexOf("\"", item.indexOf("name", item.indexOf("seller")) + 7));
        order.sellerGroupId = new StringBuilder(item).substring(item.indexOf("group_id", item.indexOf("seller")) + 10,
                item.indexOf("}", item.indexOf("group_id", item.indexOf("seller")) + 10));
    }

}
