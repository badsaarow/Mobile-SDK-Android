package com.dji.sdk.sample.internal.view;

import java.util.ArrayList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.sdk.media.MediaFile;
import dji.sdk.media.SuperResInfoBean;

public class SuperResolutionGroupMedia {

    private List<MediaFile> mSubMediaList;
    private List<MediaFile> mZoomMediaList;


    private MediaFile mMediaFile;

    private MediaFile mWideMedia;

    public SuperResolutionGroupMedia(MediaFile mediaFile) {
        mMediaFile = mediaFile;
    }

    public MediaFile getWideMedia() {
        return mWideMedia;
    }

    public List<MediaFile> getZoomMediaList() {
        return mZoomMediaList;
    }



    public SuperResInfoBean getHyperAnalyticInfo() {
        return mMediaFile.getSuperResInfoBean();
    }



}
