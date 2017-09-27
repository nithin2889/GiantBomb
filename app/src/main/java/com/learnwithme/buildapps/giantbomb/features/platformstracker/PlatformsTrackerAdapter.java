package com.learnwithme.buildapps.giantbomb.features.platformstracker;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.TrackedPlatformEntry;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;
import com.learnwithme.buildapps.giantbomb.features.platformstracker.PlatformsTrackerAdapter.PlatformViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class PlatformsTrackerAdapter extends RecyclerView.Adapter<PlatformViewHolder> {
    final OnPlatformClickListener listener;
    Cursor cursor;

    PlatformsTrackerAdapter(OnPlatformClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PlatformViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_platforms_tracker_item, parent, false);

        return new PlatformViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlatformViewHolder holder, int position) {
        holder.bindTo(position);
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    @SuppressWarnings("UnusedReturnValue")
    Cursor swapCursor(Cursor data) {
        Timber.d("Swapping cursor...");

        if (data != null) {
            Timber.d("Cursor size: " + data.getCount());
        } else {
            Timber.d("Cursor is null.");
        }

        if (cursor == data) {
            return null;
        }

        Cursor temp = cursor;
        this.cursor = data;

        if (data != null) {
            notifyDataSetChanged();
        }
        return temp;
    }

    interface OnPlatformClickListener {
        void platformClicked(long platformId);
    }

    class PlatformViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.platform_cover)
        ImageView platformCover;
        @BindView(R.id.platform_cover_progressbar)
        ProgressBar progressBar;
        @BindView(R.id.platform_name)
        TextView platformName;
        @BindView(R.id.platform_company_name)
        TextView platformCompanyName;
        @BindView(R.id.platform_release_date)
        TextView platformReleaseDate;
        private long currentId;

        PlatformViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bindTo(int position) {
            int platformIdIndex = cursor.getColumnIndexOrThrow(TrackedPlatformEntry.COLUMN_PLATFORM_ID);
            int coverImageIndex = cursor.getColumnIndexOrThrow(TrackedPlatformEntry.COLUMN_PLATFORM_SMALL_IMAGE);
            int platformNameIndex = cursor.getColumnIndexOrThrow(TrackedPlatformEntry.COLUMN_PLATFORM_NAME);
            int companyNameIndex = cursor.getColumnIndexOrThrow(TrackedPlatformEntry.COLUMN_PLATFORM_COMPANY_NAME);
            int releaseDateIndex = cursor.getColumnIndexOrThrow(TrackedPlatformEntry.COLUMN_PLATFORM_RELEASE_DATE);

            Timber.d("Cursor size is " + cursor.getCount());
            Timber.d("Cursor: platform id index is " + platformIdIndex);
            Timber.d("Cursor: cover image index is " + coverImageIndex);
            Timber.d("Cursor: platform name index is " + platformNameIndex);
            Timber.d("Cursor: company name index is " + companyNameIndex);
            Timber.d("Cursor: release date index is " + releaseDateIndex);

            cursor.moveToPosition(position);

            Timber.d("Cursor current position" + cursor.getPosition());

            Timber.d("Trying to get platform id");
            currentId = cursor.getLong(platformIdIndex);
            Timber.d("Trying to get cover image");
            String coverUrl = cursor.getString(coverImageIndex);
            Timber.d("Trying to get platform name");
            String name = cursor.getString(platformNameIndex);
            Timber.d("Trying to get company name");
            String companyName = cursor.getString(companyNameIndex);
            Timber.d("Trying to get release date");
            String releaseDate = cursor.getString(releaseDateIndex);

            // Assigning value to UI elements
            ImageUtils.loadImageWithProgress(platformCover, coverUrl, progressBar);
            platformName.setText(name);
            platformCompanyName.setText(companyName);
            platformReleaseDate.setText(DateTextUtils.getFormattedDate(releaseDate, "MMM d, yyyy"));
        }

        @Override
        public void onClick(View v) {
            listener.platformClicked(currentId);
        }
    }
}