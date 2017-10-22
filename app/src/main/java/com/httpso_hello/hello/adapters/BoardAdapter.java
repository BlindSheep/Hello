package com.httpso_hello.hello.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.httpso_hello.hello.R;
import com.httpso_hello.hello.Structures.BoardItem;
import com.httpso_hello.hello.Structures.Image;
import com.httpso_hello.hello.Structures.Photo;
import com.httpso_hello.hello.activity.BoardActivity;
import com.httpso_hello.hello.activity.ChatActivity;
import com.httpso_hello.hello.activity.FullscreenPhotoActivity;
import com.httpso_hello.hello.activity.LikeActivity;
import com.httpso_hello.hello.activity.ProfileActivity;
import com.httpso_hello.hello.helper.CircularTransformation;
import com.httpso_hello.hello.helper.Constant;
import com.httpso_hello.hello.helper.ConverterDate;
import com.httpso_hello.hello.helper.Like;
import com.httpso_hello.hello.helper.Settings;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAd;
import com.yandex.mobile.ads.nativeads.NativeAppInstallAdView;
import com.yandex.mobile.ads.nativeads.NativeContentAd;
import com.yandex.mobile.ads.nativeads.NativeContentAdView;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;

import java.util.ArrayList;

import static com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration.NATIVE_IMAGE_SIZE_LARGE;
import static com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration.NATIVE_IMAGE_SIZE_MEDIUM;
import static com.yandex.mobile.ads.nativeads.NativeAdLoaderConfiguration.NATIVE_IMAGE_SIZE_SMALL;

/**
 * Created by mixir on 02.08.2017.
 */

public class BoardAdapter extends ArrayAdapter<BoardItem> {

    private final Activity context;
    private ArrayList<BoardItem> boardItems;
    private final Settings stgs;
    private BoardActivity boardActivity;

    public BoardAdapter(Activity context, ArrayList<BoardItem> boardItems) {
        super(context, R.layout.content_board, boardItems);
        this.boardItems = boardItems;
        this.context = context;
        this.stgs = new Settings(getContext());
    }

    static class ViewHolder{
        public ImageView userAvatarBoard;
        public TextView userNameBoard;
        public TextView datePubBoard;
        public TextView writeButton;
        public TextView boardText;
        public TextView cityCache;
        public ImageView firstPhoto;
        public GridView anotherPhotoBoard;
        public ImageButton likeButtonBoard;
        public TextView likeTextBoard;
        public LinearLayout linearLayoutForPhoto;
        public TextView countReaded;
        public LinearLayout content;

        public NativeContentAdView nativeContentAdView;
        public NativeAppInstallAdView nativeAppInstallAdView;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        boardActivity = ((BoardActivity) context);

        final BoardAdapter.ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.content_board_item, null, true);
            holder = new BoardAdapter.ViewHolder();
//Виевы для объявлений
            holder.userAvatarBoard = (ImageView) rowView.findViewById(R.id.userAvatarBoard);
            holder.userNameBoard = (TextView) rowView.findViewById(R.id.userNameBoard);
            holder.datePubBoard = (TextView) rowView.findViewById(R.id.datePubBoard);
            holder.writeButton = (TextView) rowView.findViewById(R.id.writeButton);
            holder.boardText = (TextView) rowView.findViewById(R.id.boardText);
            holder.cityCache = (TextView) rowView.findViewById(R.id.cityCache);
            holder.firstPhoto = (ImageView) rowView.findViewById(R.id.firstPhotoBoard);
            holder.anotherPhotoBoard = (GridView) rowView.findViewById(R.id.anotherPhotoBoard);
            holder.likeButtonBoard = (ImageButton) rowView.findViewById(R.id.likeButtonBoard);
            holder.likeTextBoard = (TextView) rowView.findViewById(R.id.likeTextBoard);
            holder.linearLayoutForPhoto = (LinearLayout) rowView.findViewById(R.id.linearLayoutForPhoto);
            holder.countReaded = (TextView) rowView.findViewById(R.id.countReaded);
            holder.content = (LinearLayout) rowView.findViewById(R.id.content);
// Виевы для рекламы
            holder.nativeContentAdView = (NativeContentAdView) rowView.findViewById(R.id.nat);
            holder.nativeAppInstallAdView = (NativeAppInstallAdView) rowView.findViewById(R.id.natApp);

            rowView.setTag(holder);
        } else {
            holder = (BoardAdapter.ViewHolder) rowView.getTag();
        }

        if ((position % 10 != 0) || (position == 0)) {
            holder.content.setVisibility(View.VISIBLE);
            holder.nativeContentAdView.setVisibility(View.GONE);
            holder.nativeAppInstallAdView.setVisibility(View.GONE);

            final BoardItem boardItem;
            if (position < 10) boardItem = this.boardItems.get(position);
            else boardItem = this.boardItems.get(position - position / 10);

//Аватар отправителя
            if (boardItem.avatar != null) {
                Picasso
                        .with(getContext())
                        .load(Uri.parse(Constant.upload + boardItem.avatar.micro))
                        .transform(new CircularTransformation(0))
                        .into(holder.userAvatarBoard, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso
                                        .with(getContext())
                                        .load(Uri.parse(Constant.default_avatar))
                                        .transform(new CircularTransformation(0))
                                        .into(holder.userAvatarBoard);
                            }
                        });
            } else {
                Picasso
                        .with(getContext())
                        .load(Uri.parse(Constant.default_avatar))
                        .transform(new CircularTransformation(0))
                        .into(holder.userAvatarBoard);
            }

//Имя отправителя
            holder.userNameBoard.setText(boardItem.user_nickname);
//Дата объявления
            holder.datePubBoard.setText(ConverterDate.convertDateForGuest(boardItem.date_pub));
//Текст объявления
            holder.boardText.setText(Html.fromHtml(boardItem.content));
            if (boardItem.city_cache != null) holder.cityCache.setText(boardItem.city_cache);
//Кол-во просмотров
            holder.countReaded.setText(Integer.toString(boardItem.count_readed) + " просмотров");
// Первая фотография
            DisplayMetrics displaymetrics = getContext().getResources().getDisplayMetrics();
            int width = displaymetrics.widthPixels;
            holder.firstPhoto.setMinimumWidth(width);
            holder.firstPhoto.setMinimumHeight(width);
            if (boardItem.photos != null) {
                holder.firstPhoto.setVisibility(View.VISIBLE);
                Picasso
                        .with(getContext())
                        .load(Uri.parse(ConverterDate.convertUrlAvatar(boardItem.photos[0].normal)))
                        .centerCrop()
                        .resize(width, width)
                        .into(holder.firstPhoto);
                ArrayList<Photo> defolt = new ArrayList<Photo>();

                if (boardItem.photos.length > 1) {
                    holder.anotherPhotoBoard.setVisibility(View.VISIBLE);
                    for (int i = 1; i < boardItem.photos.length; i++) {
                        if (boardItem.photos[i] != null) {
                            if (boardItem.photos[i].small != null) {
                                Photo photo = new Photo();
                                photo.image = new Image();
                                photo.image.micro = ConverterDate.convertUriSubHost(boardItem.photos[i].small);
                                defolt.add(i - 1, photo);
                            }
                        }
                    }

                    holder.anotherPhotoBoard.setNumColumns(boardItem.photos.length - 1);
                    PhotosUserAdapter puAdapter = new PhotosUserAdapter(context, defolt, R.layout.content_board_item);
                    float density =
                            getContext().getResources().getDisplayMetrics().density;
                    float llColumnWidth = defolt.size() * 120 * density;
                    float llHorizontalSpacing = (defolt.size() - 1) * 4 * density;
                    float paddingLeftAndRight = 20 * density;
                    holder.linearLayoutForPhoto.getLayoutParams().width = (int) (llColumnWidth + llHorizontalSpacing + paddingLeftAndRight);
                    holder.linearLayoutForPhoto.requestLayout();
                    holder.anotherPhotoBoard.setAdapter(puAdapter);
                    holder.anotherPhotoBoard.setVisibility(View.VISIBLE);

//Клик по фотке в ленте
                    holder.anotherPhotoBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                            ArrayList<String> photoOrig = new ArrayList<String>();
                            for (int j = 0; j < boardItem.photos.length; j++) {
                                photoOrig.add(j, ConverterDate.convertUrlAvatar(boardItem.photos[j].original));
                            }
                            Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                            intent.putStringArrayListExtra("photoOrig", photoOrig);
                            intent.putExtra("likeble", false);
                            intent.putExtra("position", positions + 1);
                            boardActivity.startActivity(intent);
                        }
                    });
                } else {
                    holder.anotherPhotoBoard.setVisibility(View.GONE);
                }
            } else {
                holder.anotherPhotoBoard.setVisibility(View.GONE);
                holder.firstPhoto.setVisibility(View.GONE);
            }


//Цвет кнопки лайка
            if (!boardItem.is_voted)
                holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
            else
                holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);

//Рейтинг
            if (boardItem.rating != 0) {
                holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
            } else {
                holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
                holder.likeTextBoard.setText("Нет оценок");
            }

// обработка клика по аватарке
            holder.userAvatarBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", boardItem.user_id);
                    intent.putExtra("profile_nickname", " " + boardItem.user_nickname);
                    boardActivity.startActivity(intent);
                }
            });
// обработка клика по имени
            holder.userNameBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", boardItem.user_id);
                    intent.putExtra("profile_nickname", " " + boardItem.user_nickname);
                    boardActivity.startActivity(intent);
                }
            });
// обработка клика по дате
            holder.datePubBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("profile_id", boardItem.user_id);
                    intent.putExtra("profile_nickname", " " + boardItem.user_nickname);
                    boardActivity.startActivity(intent);
                }
            });
//обработка клика по кнопке "Написать сообщение"
            holder.writeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("contact_id", boardItem.user_id);
                    intent.putExtra("nickname", " " + boardItem.user_nickname);
                    if (boardItem.avatar == null) {
                        intent.putExtra("avatar", Constant.default_avatar);
                    } else {
                        intent.putExtra("avatar", boardItem.avatar.micro);
                    }
                    boardActivity.startActivity(intent);
                }
            });
//обработка клика по кнопке лайк
            holder.likeButtonBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!boardItem.is_voted) {
                        holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                        boardItem.is_voted = true;
                        boardItem.rating += 1;
                        holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                        holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
                        Like.getInstance(getContext()).sendLike(boardItem.id, "up", "board", "content", new Like.SendLikeCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                boardItem.is_voted = false;
                                boardItem.rating -= 1;
                                if (boardItem.rating != 0) {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                                    holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
                                } else {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
                                    holder.likeTextBoard.setText("Нет оценок");
                                }
                                Toast.makeText(getContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                                boardItem.is_voted = false;
                                boardItem.rating -= 1;
                                if (boardItem.rating != 0) {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                                    holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
                                } else {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
                                    holder.likeTextBoard.setText("Нет оценок");
                                }
                                Toast.makeText(getContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_grey_color_hello), PorterDuff.Mode.MULTIPLY);
                        boardItem.is_voted = false;
                        boardItem.rating -= 1;
                        if (boardItem.rating != 0) {
                            holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                            holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
                        } else {
                            holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
                            holder.likeTextBoard.setText("Нет оценок");
                        }
                        Like.getInstance(getContext()).sendLike(boardItem.id, "down", "board", "content", new Like.SendLikeCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(int error_code, String error_msg) {
                                holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                boardItem.is_voted = true;
                                boardItem.rating += 1;
                                if (boardItem.rating != 0) {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                                    holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
                                } else {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
                                    holder.likeTextBoard.setText("Нет оценок");
                                }
                                Toast.makeText(getContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onInternetError() {
                                holder.likeButtonBoard.getBackground().setColorFilter(getContext().getResources().getColor(R.color.main_blue_color_hello), PorterDuff.Mode.MULTIPLY);
                                boardItem.is_voted = true;
                                boardItem.rating += 1;
                                if (boardItem.rating != 0) {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_blue_color_hello));
                                    holder.likeTextBoard.setText(ConverterDate.likeStr(boardItem.rating));
                                } else {
                                    holder.likeTextBoard.setTextColor(getContext().getResources().getColor(R.color.main_grey_color_hello));
                                    holder.likeTextBoard.setText("Нет оценок");
                                }
                                Toast.makeText(getContext().getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
//обработка клика по кол-ву лайков
            holder.likeTextBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (boardItem.rating != 0) {
                        Intent intent = new Intent(getContext(), LikeActivity.class);
                        intent.putExtra("target_id", boardItem.id);
                        intent.putExtra("subject", "board");
                        intent.putExtra("target_controller", "content");
                        boardActivity.startActivity(intent);
                    } else
                        Toast.makeText(getContext().getApplicationContext(), "Нет оценок", Toast.LENGTH_LONG).show();
                }
            });

//Клик по большой фотке
            holder.firstPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> photoOrig = new ArrayList<String>();
                    for (int j = 0; j < boardItem.photos.length; j++) {
                        photoOrig.add(j, ConverterDate.convertUrlAvatar(boardItem.photos[j].original));
                    }
                    Intent intent = new Intent(getContext(), FullscreenPhotoActivity.class);
                    intent.putStringArrayListExtra("photoOrig", photoOrig);
                    intent.putExtra("likeble", false);
                    intent.putExtra("position", 0);
                    boardActivity.startActivity(intent);
                }
            });

        } else {
//Реклама
            holder.content.setVisibility(View.GONE);
            final NativeAdLoaderConfiguration adLoaderConfiguration = new NativeAdLoaderConfiguration
                    .Builder("R-M-250514-2", true)
                    .setImageSizes()
                    .build();
            NativeAdLoader mNativeAdLoader = new NativeAdLoader((BoardActivity) getContext(), adLoaderConfiguration);
            mNativeAdLoader.setOnLoadListener(new NativeAdLoader.OnLoadListener() {
                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    holder.nativeContentAdView.setVisibility(View.GONE);
                    holder.nativeAppInstallAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAppInstallAdLoaded(@NonNull NativeAppInstallAd nativeAppInstallAd) {
                    holder.nativeContentAdView.setVisibility(View.GONE);
                    holder.nativeAppInstallAdView.setVisibility(View.VISIBLE);

                    holder.nativeAppInstallAdView.setAgeView((TextView) holder.nativeAppInstallAdView.findViewById(R.id.age));
                    holder.nativeAppInstallAdView.setBodyView((TextView) holder.nativeAppInstallAdView.findViewById(R.id.body));
                    holder.nativeAppInstallAdView.setCallToActionView((Button) holder.nativeAppInstallAdView.findViewById(R.id.callToAction));
                    holder.nativeAppInstallAdView.setDomainView((TextView) holder.nativeAppInstallAdView.findViewById(R.id.domain));
                    holder.nativeAppInstallAdView.setIconView((ImageView) holder.nativeAppInstallAdView.findViewById(R.id.favicon));
                    holder.nativeAppInstallAdView.setImageView((ImageView) holder.nativeAppInstallAdView.findViewById(R.id.image));
                    holder.nativeAppInstallAdView.setSponsoredView((TextView) holder.nativeAppInstallAdView.findViewById(R.id.sponsored));
                    holder.nativeAppInstallAdView.setTitleView((TextView) holder.nativeAppInstallAdView.findViewById(R.id.title));
                    holder.nativeAppInstallAdView.setWarningView((TextView) holder.nativeAppInstallAdView.findViewById(R.id.warning));

                    try {
                        nativeAppInstallAd.bindAppInstallAd(holder.nativeAppInstallAdView);
                    } catch (NativeAdException e) {
                        holder.nativeAppInstallAdView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onContentAdLoaded(@NonNull NativeContentAd nativeContentAd) {
                    holder.nativeContentAdView.setVisibility(View.VISIBLE);
                    holder.nativeAppInstallAdView.setVisibility(View.GONE);

                    holder.nativeContentAdView.setAgeView((TextView) holder.nativeContentAdView.findViewById(R.id.content_age));
                    holder.nativeContentAdView.setBodyView((TextView) holder.nativeContentAdView.findViewById(R.id.content_body));
                    holder.nativeContentAdView.setDomainView((TextView)
                            holder.nativeContentAdView.findViewById(R.id.content_domain));
                    holder.nativeContentAdView.setIconView((ImageView) holder.nativeContentAdView.findViewById(R.id.content_favicon));
                    holder.nativeContentAdView.setImageView((ImageView) holder.nativeContentAdView.findViewById(R.id.content_image));
                    holder.nativeContentAdView.setSponsoredView((TextView) holder.nativeContentAdView.findViewById(R.id.content_sponsored));
                    holder.nativeContentAdView.setTitleView((TextView) holder.nativeContentAdView.findViewById(R.id.content_title));
                    holder.nativeContentAdView.setWarningView((TextView) holder.nativeContentAdView.findViewById(R.id.content_warning));

                    try {
                        nativeContentAd.bindContentAd(holder.nativeContentAdView);
                    } catch (NativeAdException e) {
                        holder.nativeContentAdView.setVisibility(View.GONE);
                    }
                }
            });
            mNativeAdLoader
                    .loadAd(AdRequest
                            .builder()
                            .build());
        }

//Подгрузка новых обьявлений
        BoardActivity ba = ((BoardActivity) getContext());
        if(position == (this.boardItems.size() - 2)) {
            ba.getNew();
        }

        return rowView;

    }

    public void add(ArrayList<BoardItem> boardItem) {
        this.boardItems.addAll(boardItem);
    }

    public ArrayList<BoardItem> get(){
        return this.boardItems;
    }

    public BoardItem getItem(int position){
        return this.boardItems.get(position);
    }

    public void set(ArrayList<BoardItem> boardItem){
        this.boardItems = boardItem;
        this.notifyDataSetChanged();
    }

}