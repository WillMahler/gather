package com.WKNS.gather.databaseModels;

import com.google.firebase.firestore.Exclude;

public class Photo {
    @Exclude private String localUri;
    private String remoteUri;

    public Photo(String localUri, String remoteUri){
        this.localUri = localUri;
        this.remoteUri = remoteUri;
    }

    //Setters
    public void setLocalUri(String localUri){ this.localUri = localUri; }
    public void setRemoteUri(String remoteUriUri){ this.remoteUri = remoteUri; }
    
    //Getters
    public String getLocalUri() { return localUri; }
    public String getRemoteUri() { return remoteUri; }
}
