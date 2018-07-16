package com.bedelprojects.bedel.adocmusic;

public class Api
{

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

    private static final String ROOT_URL = "http://80.211.141.148/SongsApi/v1/Api.php?apicall=";

    public static final String URL_CREAT_SONG = ROOT_URL + "insertSong";
    public static final String URL_READ_SONGS = ROOT_URL + "getSongs";
    public static final String URL_READ_STLSO = ROOT_URL + "getSongsByStyle";
    public static final String URL_READ_OSONG = ROOT_URL + "getSong";
    public static final String URL_READ_USRFA = ROOT_URL + "getUserFavs";
    public static final String URL_REGIST_FAV = ROOT_URL + "registerUserFav";
    public static final String URL_USER_EXIST = ROOT_URL + "getUser";
    public static final String URL_REGIS_USER = ROOT_URL + "registerUser";
    public static final String URL_SEARCH_FAV = ROOT_URL + "searchUserFav";
    public static final String URL_DELETE_FAV = ROOT_URL + "deleteUserFav";
    public static final String URL_READ_ACCEP = ROOT_URL + "searchUserAccepted";
    public static final String URL_REPORT_SON = ROOT_URL + "sendSongReport";
    public static final String URL_REPORT_BUG = ROOT_URL + "sendBugReport";

}
