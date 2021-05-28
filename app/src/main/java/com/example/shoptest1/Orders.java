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
    String merchant_comment;
    String weight;
    String dimensions_width;
    String dimensions_height;
    String dimensions_length;
    String quantity;

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
        this.merchant_comment = "-1";
        this.weight = "-1";
        this.dimensions_width = "-1";
        this.dimensions_height = "-1";
        this.dimensions_length = "-1";
        this.quantity = "-1";
    }

    public static void toOrder(Orders order, String item) {
        order.id = new StringBuilder(item).substring(item.indexOf("id") + 4, item.indexOf(",", item.indexOf("id")));
        order.userId = new StringBuilder(item).substring(item.indexOf("user_id") + 9, item.indexOf(",", item.indexOf("user_id")));
        order.date = new StringBuilder(item).substring(item.indexOf("date") + 6, item.indexOf(",", item.indexOf("date")));
        order.status = new StringBuilder(item).substring(item.indexOf("status") + 8, item.indexOf(",", item.indexOf("status")));
        order.itemsCount = new StringBuilder(item).substring(item.indexOf("items_count") + 13, item.indexOf(",", item.indexOf("items_count")));

        final int startTotalPriceText = item.indexOf("text", item.indexOf("total_price")) + 7;
        order.totalPriceText = new StringBuilder(item).substring(startTotalPriceText,
                item.indexOf("\"", startTotalPriceText));

        order.displayOrderId = new StringBuilder(item).substring(item.indexOf("display_order_id") + 19, item.indexOf("\"", item.indexOf("display_order_id") + 19));
        order.trackNumber = new StringBuilder(item).substring(item.indexOf("track_number") + 15, item.indexOf("\"", item.indexOf("track_number") + 15));
        order.address = new StringBuilder(item).substring(item.indexOf("address") + 10, item.indexOf("\"", item.indexOf("address") + 10));
        order.description = new StringBuilder(item).substring(item.indexOf("description") + 14, item.indexOf("\"", item.indexOf("description") + 14));
        order.thumbPhoto = new StringBuilder(item).substring(item.indexOf("thumb_photo") + 14, item.indexOf("\"", item.indexOf("thumb_photo") + 14));

        int startTitle = item.indexOf("title", item.indexOf("thumb_photo")) + 8;
        order.title = new StringBuilder(item).substring(startTitle,
                item.indexOf("\"", startTitle));

        int startSellerTitle = item.indexOf("title", item.indexOf("seller")) + 8;
        order.sellerTitle = new StringBuilder(item).substring(startSellerTitle,
                item.indexOf("\"", startSellerTitle));

        int startSellerName = item.indexOf("name", item.indexOf("seller")) + 7;
        order.sellerName = new StringBuilder(item).substring(startSellerName,
                item.indexOf("\"", startSellerName));

        int fromIndexSellerGroupId = item.indexOf("group_id", item.indexOf("seller")) + 10;
        order.sellerGroupId = new StringBuilder(item).substring(fromIndexSellerGroupId,
                item.indexOf("}", fromIndexSellerGroupId));

        order.merchant_comment = new StringBuilder(item).substring(item.indexOf("merchant_comment") + 19,
                item.indexOf("\"" , item.indexOf("merchant_comment") + 19));
        order.weight = new StringBuilder(item).substring(item.indexOf("weight") + 8,
                item.indexOf("\"", item.indexOf("weight") + 8));

        int startDimensions_width = item.indexOf("width", item.indexOf("dimensions")) + 7;
        order.dimensions_width = new StringBuilder(item).substring(startDimensions_width,
                item.indexOf(",", startDimensions_width));

        int startDimensions_height = item.indexOf("height", item.indexOf("dimensions")) + 8;
        order.dimensions_height = new StringBuilder(item).substring(startDimensions_height,
                item.indexOf(",", startDimensions_height));

        int startDimensions_length = item.indexOf("length", item.indexOf("dimensions")) + 8;
        order.dimensions_length = new StringBuilder(item).substring(startDimensions_length,
                item.indexOf("}", startDimensions_length));

        order.quantity = new StringBuilder(item).substring(item.indexOf("quantity") + 10,
                item.indexOf(",", item.indexOf("quantity") + 10));

    }

}
