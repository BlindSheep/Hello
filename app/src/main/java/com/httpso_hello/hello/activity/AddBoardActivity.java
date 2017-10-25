package com.httpso_hello.hello.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.*;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.Attachment;
import com.httpso_hello.hello.adapters.FilesAdapter;
import com.httpso_hello.hello.adapters.MessagesAttachmentsAdapter;
import com.httpso_hello.hello.helper.*;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class AddBoardActivity extends SuperMainActivity {

    private TextView boardCancel;
    private TextView boardSave;
    private EditText boardText;
    private TextView boardPhotos;
    private String photos = "";
    private String boardTextString = "";
    private Uri sendingImageUri;
    private FilesAdapter faAdapter;
    private GridView filesLine;
    private Files files;
    private CheckBox anonim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);
        setHeader();
        setMenuItem("BoardActivity");

        boardCancel = (TextView) findViewById(R.id.boardCancel);
        boardSave = (TextView) findViewById(R.id.boardSave);
        boardText = (EditText) findViewById(R.id.boardText);
        boardPhotos = (TextView) findViewById(R.id.boardPhotos);
        anonim = (CheckBox) findViewById(R.id.anonim);

        filesLine = (GridView) findViewById(R.id.addBoardPhotos);

        files = new Files(getApplicationContext(), this);

        //Кнопка "Добавить фото"
        boardPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        //Кнопка "Отмена"
        boardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Кнопка "Сохранить"
        boardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardTextString = boardText.getText().toString();
                if ((!boardTextString.isEmpty()) /*&& (!photos.isEmpty())*/) {
                    HBoard hBoard = new HBoard(getApplicationContext());
                    hBoard.addBoard(
                            boardTextString,
                            anonim.isChecked(),
                            files.getUploadedFiles(),
                            new HBoard.AddBoardCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getApplicationContext(), "Объявление появится после модерации", Toast.LENGTH_LONG).show();
                                    finish();
                                }


                            }, new Help.ErrorCallback() {
                                @Override
                                public void onError(int error_code, String error_message) {
                                    Toast.makeText(getApplicationContext(), "Объявление появится после модерации", Toast.LENGTH_LONG).show();
                                    finish();
                                }

                                @Override
                                public void onInternetError() {
                                    Toast.makeText(getApplicationContext(), "Объявление появится после модерации", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                } else if (boardTextString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Напишите текст объявления", Toast.LENGTH_LONG).show();
                } else if (photos.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Добавьте хотябы одну фотографию", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //Обрабатываем результат выбора фоток из галереи или с камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    sendingImageUri = imageReturnedIntent.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(Help.runTaskAfterPermission(
                                AddBoardActivity.this,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                Help.REQUEST_ADD_PHOTO_MESSAGE
                        )){
                            sendImageFromGallery();
                        }


                    }
                }
                break;
            default:
                System.out.println("Какая-то ошибка");
                break;
        }
    }

    public void sendImageFromGallery(){
        try {
            final InputStream imageStream = getContentResolver().openInputStream(this.sendingImageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            int position = 0;
            if(faAdapter != null){
                Attachment defoltAttachment = new Attachment();
                defoltAttachment.previewAttachmentUri = this.sendingImageUri;
                position = faAdapter.addAttachment(defoltAttachment);
            } else {

                Attachment defoltAttachment = new Attachment();
                defoltAttachment.previewAttachmentUri = this.sendingImageUri;
                ArrayList<Attachment> defoltListAttachment = new ArrayList<>();
                defoltListAttachment.add(defoltAttachment);

                faAdapter = new FilesAdapter(
                        AddBoardActivity.this,
                        defoltListAttachment,
                        R.layout.content_add_board
                );
                this.filesLine.setAdapter(faAdapter);
                filesLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Attachment attachment = maAdapter.getItem(position);
//                        messages.deleteAttachment(position, attachment.id);
//                        maAdapter.deleteAttachment(position);
                    }
                });
            }

            filesLine.getLayoutParams().width =
                    filesLine.getLayoutParams().width +
                            Help.getPxFromDp(130, this);

            //Преобразование для отправки на серв
            final String file_base64 = Help.getBase64FromImage(
                    selectedImage,
                    Bitmap.CompressFormat.JPEG,
                    Help.getFileSize(sendingImageUri, getApplicationContext()),
                    0
            );
            files.uploadFile(
                    "photo",
                    "jpg",
                    file_base64,
                    "content",
                    "board",
                    position,
                    new Files.UploadFileCallback() {
                        @Override
                        public void onSuccess(int id, int position) {
                            faAdapter.setLoadedFile(position, id);
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {

                        }

                        @Override
                        public void onInternetError() {

                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
