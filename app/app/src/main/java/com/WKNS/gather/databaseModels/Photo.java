package com.WKNS.gather.databaseModels;

import com.google.firebase.firestore.Exclude;

public class Photo {
    @Exclude private String localUri;
    private String remoteUri;

    public Photo(String localUri, String remoteUri){
        this.localUri = localUri;
        this.remoteUri = remoteUri;
    }

    public String getLocalUri() {
        return localUri;
    }

    public String getRemoteUri() {
        return remoteUri;
    }
}
