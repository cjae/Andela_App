package com.esod.andelaapp.model;

/**
 * Created by Jedidiah on 08/03/2017.
 */

public class Developer {

    private int _id;
    private String _login;
    private String _avatar_url;
    private String _url;
    private int _bg_color;

    public Developer(int _id, String _login, String _avatar_url, String _url, int _bg_color) {
        this._id = _id;
        this._login = _login;
        this._avatar_url = _avatar_url;
        this._url = _url;
        this._bg_color = _bg_color;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_login() {
        return _login;
    }

    public void set_login(String _login) {
        this._login = _login;
    }

    public String get_avatar_url() {
        return _avatar_url;
    }

    public void set_avatar_url(String _avatar_url) {
        this._avatar_url = _avatar_url;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public int get_bg_color() {
        return _bg_color;
    }

    public void set_bg_color(int _bg_color) {
        this._bg_color = _bg_color;
    }
}
