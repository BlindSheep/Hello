package com.httpso_hello.hello.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

public class AddBoardActivity extends AppCompatActivity {

    private TextView boardCancel;
    private ImageView boardSave;
    private EditText boardText;
    private ImageButton boardPhotos;
    private String photos = "";
    private String boardTextString = "";
    private Uri sendingImageUri;
    private FilesAdapter faAdapter;
    private GridView filesLine;
    private Files files;
    private CheckBox anonim;
    private int groupId = 0;
    private boolean isModeratble;
    private ProgressBar progress;
    private boolean uploadingFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);

        groupId = getIntent().getExtras().getInt("groupId", 0);
        isModeratble = getIntent().getExtras().getBoolean("isModeratble", true);

        boardCancel = (TextView) findViewById(R.id.boardCancel);
        boardSave = (ImageView) findViewById(R.id.boardSave);
        boardText = (EditText) findViewById(R.id.boardText);
        boardPhotos = (ImageButton) findViewById(R.id.boardPhotos);
        anonim = (CheckBox) findViewById(R.id.anonim);
        progress = (ProgressBar) findViewById(R.id.progress);

        filesLine = (GridView) findViewById(R.id.addBoardPhotos);

        files = new Files(getApplicationContext(), this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Кнопка "Добавить фото"
        boardPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        //Кнопка "Сохранить"
        boardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                boardSave.setVisibility(View.GONE);
                boardTextString = boardText.getText().toString();
                if(!uploadingFile) {
                    if ((!boardTextString.isEmpty()) || (files.getUploadedFiles().size() != 0)) {
                        HBoard hBoard = new HBoard(getApplicationContext());
                        hBoard.addBoard(
                                boardTextString,
                                groupId,
                                anonim.isChecked(),
                                files.getUploadedFiles(),
                                new HBoard.AddBoardCallback() {
                                    @Override
                                    public void onSuccess() {
                                        if (isModeratble)
                                            Toast.makeText(getApplicationContext(), "Сообщение появится после модерации", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(getApplicationContext(), "Сообщение успешно отправлено", Toast.LENGTH_LONG).show();
                                        finish();
                                    }


                                }, new Help.ErrorCallback() {
                                    @Override
                                    public void onError(int error_code, String error_message) {
                                        Toast.makeText(getApplicationContext(), "Ошибка интернет соединения. Попробуйте еще раз.", Toast.LENGTH_LONG).show();
                                        progress.setVisibility(View.GONE);
                                        boardSave.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onInternetError() {
                                        Toast.makeText(getApplicationContext(), "Ошибка интернет соединения. Попробуйте еще раз.", Toast.LENGTH_LONG).show();
                                        progress.setVisibility(View.GONE);
                                        boardSave.setVisibility(View.VISIBLE);
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Напишите текст сообщения", Toast.LENGTH_LONG).show();
                        progress.setVisibility(View.GONE);
                        boardSave.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Дождитесь завершения загрузки файлов", Toast.LENGTH_LONG).show();
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
                    } else {
                        sendImageFromGallery();
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
            uploadingFile = true;
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
            String tag = files.uploadFile(
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
                            uploadingFile = false;
                        }
                    }, new Help.ErrorCallback() {
                        @Override
                        public void onError(int error_code, String error_msg) {
                            uploadingFile = false;
                        }

                        @Override
                        public void onInternetError() {
                            uploadingFile = false;
                        }
                    });
            faAdapter.setRequestTag(position, tag);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Кнопка назад(переход на страницу входа)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
