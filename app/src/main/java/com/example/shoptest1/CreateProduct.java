package com.example.shoptest1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateProduct extends AppCompatActivity {
// this part of code isn't ready
// this part of code isn't ready
// this part of code isn't ready
// this part of code isn't ready
// this part of code isn't ready
// this part of code isn't ready
// this part of code isn't ready

    public static String mainPhotoUriStr = new String();

    EditText productName, description, price;
    String accessToken = new String(), ownerId = new String(), respStr = new String();
    Button done;
    Button choosePhoto, savePhoto;
    String  category = new String();
    Uri mainPhotoUri;
    private String uploadUrl = new String();
    String uploadUrlWithoutBrackets = new String(), uploadUrlWithoutBracketsWithSlashes = new String();

    String photo = new String();
    String server = new String();
    String hash = new String();
    String crop_data = new String();
    String crop_hash = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        accessToken = getIntent().getStringExtra("access_token");
        ownerId = getIntent().getStringExtra("group_id");
        done = findViewById(R.id.done);
        choosePhoto = findViewById(R.id.choosePhoto);
        savePhoto = findViewById(R.id.save_image);
        Spinner spinner = findViewById(R.id.spinnerCategory);
        done = findViewById(R.id.done);
        choosePhoto = findViewById(R.id.choosePhoto);
        productName = findViewById(R.id.editProductName);
        description = findViewById(R.id.editDescription);
        price = findViewById(R.id.editPrice);

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrlPhoto();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Constants.GALLERY_REQUEST);
                uploadUrlWithoutBrackets = new StringBuilder(uploadUrl).substring(uploadUrl.indexOf("https"), uploadUrl.indexOf("\"", uploadUrl.indexOf("https")));
                for (int i = 0; i < uploadUrlWithoutBrackets.length(); i++) {
                    if (uploadUrlWithoutBrackets.charAt(i) == '\\') {
                        continue;
                    }
                    uploadUrlWithoutBracketsWithSlashes += uploadUrlWithoutBrackets.charAt(i);
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCall postCall = new PostCall();
                String resp = new String();
                try {
                    resp = postCall.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public Boolean uploadFile(String serverURL, File file, OkHttpClient client, String groupId) {
        try {

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("image/jpeg"), file))
                    .build();

            Request request = new Request.Builder()
                    .url(serverURL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(final Call call, final IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {

                    }
                    respStr = response.body().string();

                    parseResponseUploadUrl(respStr);

                    saveMarketPhoto();

                }
            });


            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public class PostCall extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            File photoFile = new File(mainPhotoUriStr);
            return String.valueOf(uploadFile(uploadUrlWithoutBracketsWithSlashes, photoFile,
                    Constants.client, ownerId));
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private String getUrlPhoto() {
        String url = "https://api.vk.com/method/photos.getMarketUploadServer?v=5.52&access_token=" +
                accessToken + "&group_id=" + ownerId + "&main_photo=1";
        GetCall getCall = new GetCall();
        try {
            uploadUrl = getCall.execute(url).get();
            Log.d("URLPHOTO", uploadUrl);
            return uploadUrl;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return uploadUrl;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    mainPhotoUri = data.getData();
                    mainPhotoUriStr = getRealPathFromURI(mainPhotoUri);
                    Log.d("URIPHOTO", mainPhotoUriStr);
                }
                break;
        }
    }

    private void getCategory(Spinner spinner) {
        switch (spinner.getSelectedItem().toString()) {
            case ("Женская одежда"):
                category = "1";
                break;
            case ("Мужская одежда"):
                category = "2";
                break;
            case ("Детская одежда"):
                category = "3";
                break;
            case ("Обувь и сумки"):
                category = "4";
                break;
            case ("Аксессуары и украшения"):
                category = "5";
                break;
            case ("Автокресла"):
                category = "100";
                break;
            case ("Детские коляски"):
                category = "101";
                break;
            case ("Детская комната"):
                category = "102";
                break;
            case ("Игрушки"):
                category = "103";
                break;
            case ("Мамам и малышам"):
                category = "104";
                break;
            case ("Обучение и творчество"):
                category = "105";
                break;
            case ("Школьникам"):
                category = "106";
                break;
            case ("Телефоны и аксессуары"):
                category = "200";
                break;
            case ("Фото- и видеокамеры"):
                category = "201";
                break;
            case ("Аудио- и видеотехника"):
                category = "202";
                break;
            case ("Портативная техника"):
                category = "203";
                break;
            case ("Игровые приставки и игры"):
                category = "204";
                break;
            case ("Техника для автомобилей"):
                category = "205";
                break;
            case ("Оптические приборы"):
                category = "206";
                break;
            case ("Компьютеры"):
                category = "300";
                break;
            case ("Ноутбуки, нетбуки"):
                category = "301";
                break;
            case ("Комплектующие и аксессуары"):
                category = "302";
                break;
            case ("Периферийные устройства"):
                category = "303";
                break;
            case ("Сетевое оборудование"):
                category = "304";
                break;
            case ("Оргтехника и расходники"):
                category = "305";
                break;
            case  ("Фильмы, музыка, программы"):
                category = "306";
                break;
            case ("Автомобили"):
                category = "400";
                break;
            case ("Мотоциклы и мототехника"):
                category = "401";
                break;
            case ("Грузовики и спецтехника"):
                category = "402";
                break;
            case ("Водный транспорт"):
                category = "403";
                break;
            case ("Запчасти и аксессуары"):
                category = "404";
                break;
            case ("Квартиры"):
                category = "500";
                break;
            case ("Комнаты"):
                category = "501";
                break;
            case ("Дома, дачи, коттеджи"):
                category = "502";
                break;
            case ("Земельные участки"):
                category = "503";
                break;
            case ("Гаражи и машиноместа"):
                category = "504";
                break;
            case ("Коммерческая недвижимость"):
                category = "505";
                break;
            case ("Недвижимость за рубежом"):
                category = "506";
                break;
            case ("Бытовая техника"):
                category = "600";
                break;
            case ("Мебель и интерьер"):
                category = "601";
                break;
            case ("Кухонные принадлежности"):
                category = "602";
                break;
            case ("Текстиль"):
                category = "603";
                break;
            case ("Хозяйственные товары"):
                category = "604";
                break;
            case ("Ремонт и строительство"):
                category = "605";
                break;
            case ("Дача, сад и огород"):
                category = "606";
                break;
            case ("Декоративная косметика"):
                category = "700";
                break;
            case ("Парфюмерия"):
                category = "701";
                break;
            case ("Уход за лицом и телом"):
                category = "702";
                break;
            case ("Приборы и аксессуары"):
                category = "703";
                break;
            case ("Оптика"):
                category = "704";
                break;
            case ("Активный отдых"):
                category = "800";
                break;
            case ("Туризм"):
                category = "801";
                break;
            case ("Охота и рыбалка"):
                category = "802";
                break;
            case ("Тренажеры и фитнес"):
                category = "803";
                break;
            case ("Игры"):
                category = "804";
                break;
            case ("Билеты и путешествия"):
                category = "900";
                break;
            case ("Книги и журналы"):
                category = "901";
                break;
            case ("Коллекционирование"):
                category = "902";
                break;
            case ("Музыкальные инструменты"):
                category = "903";
                break;
            case ("Настольные игры"):
                category = "904";
                break;
            case ("Подарочные наборы и сертификаты"):
                category = "905";
                break;
            case ("Сувениры и цветы"):
                category = "906";
                break;
            case ("Рукоделие"):
                category = "907";
                break;
            case ("Собаки"):
                category = "1000";
                break;
            case ("Кошки"):
                category = "1001";
                break;
            case ("Грызуны"):
                category = "1002";
                break;
            case ("Птицы"):
                category = "1003";
                break;
            case ("Рыбы"):
                category = "1004";
                break;
            case ("Другие животные"):
                category = "1005";
                break;
            case ("Корма и аксессуары"):
                category = "1006";
                break;
            case ("Бакалея"):
                category = "1100";
                break;
            case ("Биопродукты"):
                category = "1101";
                break;
            case ("Детское питание"):
                category = "1102";
                break;
            case ("Еда на заказ"):
                category = "1103";
                break;
            case ("Напитки"):
                category = "1104";
                break;
            case ("Фото- и видеосъёмка"):
                category = "1200";
                break;
            case ("Удалённая работа"):
                category = "1201";
                break;
            case ("Организация мероприятий"):
                category = "1202";
                break;
            case ("Красота и здоровье"):
                category = "1203";
                break;
            case ("Установка и ремонт техники"):
                category = "1204";
                break;
            case ("Уборка и помощь по хозяйству"):
                category = "1205";
                break;
            case ("Курьеры и грузоперевозки"):
                category = "1206";
                break;
            case ("Обучение и развитие"):
                category = "1207";
                break;
        }
    }

    private void addProduct() {
        String url = "https://api.vk.com/method/market.add?v=5.52&access_token=" + accessToken +
                "&owner_id=-" + ownerId + "&name=" + productName.getText().toString() + "&description=" +
                description.getText().toString() + "&category_id=" + category + "&price=" + price.getText().toString();
        GetCall getCall = new GetCall();
        String newProductId = new String();
        try {
            newProductId = getCall.execute(url).get();
            Log.d("URL", url);
            Log.d("NEWPRODUCTID", newProductId);
            Intent i = new Intent(CreateProduct.this, GroupMarket.class);
            setResult(1, i);
            finish();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void parseResponseUploadUrl(String respStr) {
        server = new StringBuilder(respStr).substring(respStr.indexOf("server") + 8,
                respStr.indexOf(",", respStr.indexOf("server") + 8));

        int startOfPhoto = respStr.indexOf("photo") + 8;
        int endOfPhoto =  respStr.indexOf("}]\"", respStr.indexOf("photo") + 8);
        for (int i = startOfPhoto; i <= endOfPhoto; i++) {
            /*
            if (respStr.charAt(i) == '\\') {
                continue;
            }
             */

            if (respStr.charAt(i) == '\"' || respStr.charAt(i) == '\\') {
                continue;
            }
            photo += respStr.charAt(i);
        }

        hash = new StringBuilder(respStr).substring(respStr.indexOf("hash") + 7,
                respStr.indexOf("\"", respStr.indexOf("hash") + 7));

        int startOfCropData = respStr.indexOf("crop_data") + 12;
        if (respStr.charAt(startOfCropData) == '{') {
            //crop_data = new StringBuilder(respStr).substring(startOfCropData, respStr.indexOf("}\"", startOfCropData));
            int endOfCropData = respStr.indexOf("}\"", startOfCropData);
            for (int i = startOfCropData; i <= endOfCropData; i++) {
                if (respStr.charAt(i) == '\"' || respStr.charAt(i) == '\\') {
                    continue;
                }
                crop_data += respStr.charAt(i);
            }
        } else {
            crop_data = new StringBuilder(respStr).substring(startOfCropData,
                    respStr.indexOf("\"", startOfCropData));
        }

        crop_hash = new StringBuilder(respStr).substring(respStr.indexOf("crop_hash") + 12,
                respStr.indexOf("\"", respStr.indexOf("crop_hash") + 12));
    }

    private void saveMarketPhoto() {

        String url = "https://api.vk.com/method/photos.saveMarketPhoto?v=5.131&access_token=" +
                accessToken + "&group_id=" + ownerId + "&photo=" + photo + "&server=" + server +
                "&hash=" + hash + "&crop_data=" + crop_data + "&crop_hash=" + crop_hash;


        /*
        String url = "https://api.vk.com/method/photos.saveMarketPhoto?v=5.131&access_token=" +
                accessToken + "&group_id=" + ownerId + "&photo=[{&#092;&quot;photo&#092;&quot;:&#092;&quot;7bd1ca106b:x&#092;&quot;,&#092;&quot;sizes&#092;&quot;:[[&#092;&quot;s&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37ad9&#092;&quot;,&#092;&quot;DvnfaTm33hY&#092;&quot;,75,75],[&#092;&quot;m&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37ada&#092;&quot;,&#092;&quot;cEX9d-SUy2A&#092;&quot;,130,130],[&#092;&quot;x&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37adb&#092;&quot;,&#092;&quot;RuYsz_jLHgs&#092;&quot;,604,604],[&#092;&quot;o&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37adc&#092;&quot;,&#092;&quot;UHcixoGaPcw&#092;&quot;,130,130],[&#092;&quot;p&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37add&#092;&quot;,&#092;&quot;Rvl9zpgbhH0&#092;&quot;,200,200],[&#092;&quot;q&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37ade&#092;&quot;,&#092;&quot;ZCI6XRFcP4g&#092;&quot;,320,320],[&#092;&quot;r&#092;&quot;,&#092;&quot;633819852&#092;&quot;,&#092;&quot;37adf&#092;&quot;,&#092;&quot;qz84miJ-YJQ&#092;&quot;,510,510]],&#092;&quot;kid&#092;&quot;:&#092;&quot;8dd560da8e49366f79556497341862c4&#092;&quot;,&#092;&quot;debug&#092;&quot;:&#092;&quot;xsxmxxxoxpxqxrx&#092;&quot;}]"
                + "&server=" + server +
                "&hash=" + hash + "&crop_data=" + crop_data + "&crop_hash=" + crop_hash;

         */
        Log.d("ABABADBDBA", url);
        GetCall getCall = new GetCall();
        try {
            String resp = getCall.execute(url).get();
            Log.d("ABABADBDBA1", resp);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}