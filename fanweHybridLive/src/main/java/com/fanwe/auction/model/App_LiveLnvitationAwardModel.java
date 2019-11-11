package com.fanwe.auction.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.io.Serializable;

public class App_LiveLnvitationAwardModel extends BaseActModel implements Serializable {

    private LiveLnvitationAwardModel liveLnvitationAwardModel;

    public LiveLnvitationAwardModel getLiveLnvitationAwardModel() {
        return liveLnvitationAwardModel;
    }

    public void setLiveLnvitationAwardModel(LiveLnvitationAwardModel liveLnvitationAwardModel) {
        this.liveLnvitationAwardModel = liveLnvitationAwardModel;
    }
}
