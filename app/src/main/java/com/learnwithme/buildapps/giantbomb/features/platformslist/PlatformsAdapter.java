package com.learnwithme.buildapps.giantbomb.features.platformslist;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.model.GameCompanyInfoShort;
import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;
import com.learnwithme.buildapps.giantbomb.features.platformslist.PlatformsAdapter.PlatformViewHolder;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
class PlatformsAdapter extends RecyclerView.Adapter<PlatformViewHolder> {

    final OnPlatformClickListener listener;
    private List<GamePlatformInfoList> platforms;

    PlatformsAdapter(OnPlatformClickListener listener) {
        platforms = new ArrayList<>(0);
        this.listener = listener;
    }

    @Override
    public PlatformViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_platforms_list_item, parent, false);

        return new PlatformViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlatformViewHolder holder, int position) {
        holder.bindTo(platforms.get(position));
    }

    @Override
    public int getItemCount() {
        return platforms == null ? 0 : platforms.size();
    }

    List<GamePlatformInfoList> getPlatforms() {
        return platforms;
    }

    void setPlatforms(List<GamePlatformInfoList> platforms) {
        this.platforms = platforms;
    }

    interface OnPlatformClickListener {
        void platformClicked(long platformsId);
    }

    class PlatformViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.platform_image_layout)
        FrameLayout imageLayout;
        @BindView(R.id.platform_cover_image)
        ImageView platformCover;
        @BindView(R.id.platform_cover_progressbar)
        ProgressBar progressBar;
        @BindView(R.id.platform_name)
        TextView platformName;
        @BindView(R.id.platform_company_name)
        TextView platformCompanyName;
        @BindView(R.id.platform_release_date)
        TextView platformReleaseDate;

        @BindString(R.string.platforms_company_text)
        String platformCompanyFormat;
        @BindString(R.string.platforms_release_date)
        String platformReleaseDateFormat;
        private long currentPlatformId;

        PlatformViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bindTo(GamePlatformInfoList platform) {
            currentPlatformId = platform.id();

            GameImages image = platform.image();
            if(image != null) {
                String url = image.small_url();
                ImageUtils.loadImageWithProgress(platformCover, url, progressBar);
            } else {
                imageLayout.setVisibility(View.GONE);
            }

            platformName.setText(platform.name());

            GameCompanyInfoShort company = platform.company();
            if(company != null) {
                String companyName = String.format(Locale.US, platformCompanyFormat, company.name());
                platformCompanyName.setText(companyName);
            } else {
                platformCompanyName.setVisibility(View.GONE);
            }

            String releasedDate = platform.release_date();
            if(releasedDate != null) {
                String releaseDate = String.format(Locale.US, platformReleaseDateFormat, platform.release_date());
                platformReleaseDate.setText(DateTextUtils.getFormattedDate(releaseDate, "MMM d, yyyy"));
            } else {
                platformReleaseDate.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            listener.platformClicked(currentPlatformId);
        }
    }
}