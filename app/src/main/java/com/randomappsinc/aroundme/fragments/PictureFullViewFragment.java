package com.randomappsinc.aroundme.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PictureFullViewFragment extends Fragment {

    public static final String IMAGE_URL_KEY = "imageUrl";

    public static PictureFullViewFragment newInstance(String imageUrl) {
        PictureFullViewFragment fragment = new PictureFullViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL_KEY, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    private final Callback imageLoadingCallback = new Callback() {
        @Override
        public void onSuccess() {
            parent.animate().alpha(1.0f).setDuration(getResources().getInteger(R.integer.default_anim_length));
        }

        @Override
        public void onError(Exception exception) {
            Toast.makeText(getActivity(), R.string.image_load_fail, Toast.LENGTH_LONG).show();
        }
    };

    @BindView(R.id.parent) View parent;
    @BindView(R.id.picture) ImageView picture;

    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.picture_full_view_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Drawable defaultThumbnail = new IconDrawable(getActivity(), IoniconsIcons.ion_image).colorRes(R.color.dark_gray);
        String imageUrl = getArguments().getString(IMAGE_URL_KEY);
        Picasso.get()
                .load(imageUrl)
                .error(defaultThumbnail)
                .fit()
                .centerInside()
                .into(picture, imageLoadingCallback);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
