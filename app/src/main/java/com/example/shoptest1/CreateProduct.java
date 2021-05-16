package com.example.shoptest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;


import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateProduct extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    public static String mainPhotoUriStr = new String();
    //OkHttpClient client = new OkHttpClient();

    EditText productName, description, price, dimension_width, dimension_height, dimension_length,
            weight, sku;
    String accessToken = new String(), ownerId = new String();
    Button done;
    Button choosePhoto;
    String  category = new String();
    Uri mainPhotoUri;
    private String uploadUrl = new String();
    String uploadUrlWithoutBrackets = new String(), uploadUrlWithoutBracketsWithSlashes = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        accessToken = getIntent().getStringExtra("access_token");
        ownerId = getIntent().getStringExtra("group_id");
        done = findViewById(R.id.done);
        choosePhoto = findViewById(R.id.choosePhoto);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrlPhoto();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                uploadUrlWithoutBrackets = new StringBuilder(uploadUrl).substring(uploadUrl.indexOf("https"), uploadUrl.indexOf("\"", uploadUrl.indexOf("https")));
                for (int i = 0; i < uploadUrlWithoutBrackets.length(); i++) {
                    if (uploadUrlWithoutBrackets.charAt(i) == '\\') {
                        //uploadUrlWithoutBracketsWithSlashes += '\\';
                        continue;
                    }
                    uploadUrlWithoutBracketsWithSlashes += uploadUrlWithoutBrackets.charAt(i);
                }
                Log.d("WEHERE", uploadUrlWithoutBracketsWithSlashes);
                Log.d("VERSION", String.valueOf(Build.VERSION.SDK_INT));


                //Log.d("URLPHOTO1", uploadUrlWithoutBrackets);
                //Log.d("URIPHOTO", mainPhotoUriStr);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCall postCall = new PostCall();
                try {
                    String s = postCall.execute().get();
                    Log.d("POSTCALL", s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });





        /*

        String uploadUrl = new String();

        //Log.d("GROUPID1", ownerId);
        Spinner spinner = findViewById(R.id.spinnerCategory);
        done = findViewById(R.id.done);
        choosePhoto = findViewById(R.id.choosePhoto);
        productName = findViewById(R.id.editProductName);
        description = findViewById(R.id.editDescription);
        price = findViewById(R.id.editPrice);
        dimension_width = findViewById(R.id.edit_dimension_width);
        dimension_height = findViewById(R.id.edit_dimension_height);
        dimension_length = findViewById(R.id.edit_dimension_length);
        weight = findViewById(R.id.editWeight);
        sku = findViewById(R.id.editSku);
        String selected = spinner.getSelectedItem().toString();
        //Log.d("SPINNER", selected);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategory(spinner);
                addProduct();
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrlPhoto();
                Log.d("URLPHOTO1", uploadUrl);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

         */

    }

    public class PostCall extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpClient client = HttpClientBuilder.create().build();
            File filePhoto = new File(mainPhotoUriStr);
            HttpPost post = new HttpPost(uploadUrlWithoutBracketsWithSlashes);
            FileBody fileBody = new FileBody(filePhoto, ContentType.DEFAULT_BINARY);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", fileBody);
            HttpEntity entity = builder.build();

            post.setEntity(entity);
            try {
                HttpResponse response = client.execute(post);
                Log.d("RESPONSE", response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    mainPhoto = data.getData();
                    mainPhotoStr = mainPhoto.toString();
                }
                break;
        }
    }

     */

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
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    mainPhotoUri = data.getData();
                    mainPhotoUriStr = mainPhotoUri.toString();
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
}