package com.httpso_hello.hello.Structures;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Общий on 25.09.2017.
 */

public class Attachment {
// ID после загрузки
    public int id;
    // Тип прикладываемого файла
    public String type;
    // Пути до загруженных изображений
    public Photo photo;
    // Строковое представлвение до прикладываемого файла на устройстве
    public String pathPreviewAttachment;
    // URI до preview изображения прикладываемого файла
    public Uri previewAttachmentUri;
    // Ширина превью изображения прикладываемого файла
    public int widthPreviewAttachment;
    // Флаг, устанавливается в true когда прикладываемый файл загружен на сервер
    public boolean isUploaded;
    // Тэг request-а загрузки на сервер
    public String requestTag;


    public Attachment(){

    }
}
