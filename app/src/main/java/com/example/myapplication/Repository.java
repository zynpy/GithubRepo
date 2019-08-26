package com.example.myapplication;

import java.io.Serializable;

public class Repository  implements Serializable, Cloneable{
    private String repoName;
    private String avatarURL;
    private String owner;
    private int stars;
    private String openIssue;
    private boolean favoruite;

    public Repository(String repoName, String avatarURL, String owner, int stars, String openIssue) {
        this.repoName = repoName;
        this.avatarURL = avatarURL;
        this.owner = owner;
        this.stars = stars;
        this.openIssue = openIssue;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getOpenIssue() {
        return openIssue;
    }

    public void setOpenIssue(String openIssue) {
        this.openIssue = openIssue;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public boolean isFavoruite() {
        return favoruite;
    }

    public void setFavoruite(boolean favoruite) {
        this.favoruite = favoruite;
    }
}
