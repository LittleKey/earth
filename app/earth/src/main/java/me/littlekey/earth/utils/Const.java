package me.littlekey.earth.utils;

/**
 * Created by nengxiangzhou on 15/12/24.
 */
public class Const {
  public static final String LAST_ACTION = "last_action";
  public static final String LAST_SESSION_ID = "last_session_id";
  public static final String LAST_USER_ID = "last_user_id";
  public static final String LAST_PASS_HASH = "last_pass_hash";
  public static final String LAST_CONTACT = "last_contact";
  public static final String LAST_REGIONS_ID = "last_regions_id";
  public static final String LAST_REGION_TEXT = "last_region_text";
  public static final String FIRST_SYNC = "first_sync";
  public static final String EMPTY_STRING = "";
  /*********************************** Action **********************************/
  public static final Integer ACTION_MAIN = 0;
  /************************************ Key ***********************************/
  public static final String KEY_EXTRA = "extra";
  public static final String KEY_API_TYPE = "api_url";
  public static final String KEY_API_QUERY = "api_query";
  public static final String KEY_ID = "id";
  public static final String KEY_MODEL = "model";
  public static final String KEY_NEW_PASSWORD = "new_password";
  public static final String KEY_OLD_PASSWORD = "old_password";
  public static final String KEY_MOBILE_PN = "mobile_pn";
  public static final String KEY_VERIFICATION_CODE = "verification_code";
  public static final String KEY_USER_NAME = "username";
  public static final String KEY_PASSWORD = "password";
  public static final String KEY_QUERY = "query";
  public static final String KEY_IDENTITY = "identity";
  public static final String KEY_AVATAR = "avatar";
  public static final String KEY_GENDER = "gender";
  public static final String KEY_REGION = "region";
  public static final String KEY_USER_ID = "user_id";
  public static final String KEY_TYPE = "type";
  public static final String KEY_GROUP_COUNT = "group_count";
  public static final String KEY_BUNDLE = "bundle";
  public static final String KEY_DISPLAY_NAME = "display_name";
  public static final String KEY_BIO = "bio";
  public static final String KEY_HEIGHT = "height";
  public static final String KEY_COOKIE = "cookie";
  /*********************************** EXTRA **********************************/
  public static final String EXTRA_TASK_ID = KEY_EXTRA + "task_id";
  public static final String EXTRA_IDENTITY = KEY_EXTRA + "identity";
  public static final String EXTRA_UPLOAD_IMAGE = KEY_EXTRA + "upload_image";
  public static final String EXTRA_SELECT_IMAGE = KEY_EXTRA + "select_image";
  public static final String EXTRA_IMAGE_TYPE = KEY_EXTRA + "image_type";
  public static final String EXTRA_SOURCE_IMAGE_URI = KEY_EXTRA + "source_uri";
  public static final String EXTRA_DEST_IMAGE_URI = KEY_EXTRA + "dest_uri";
  public static final String EXTRA_BORDER_TYPE = KEY_EXTRA + "draw_circle_border";
  public static final String EXTRA_ROOM_IDENTITY = KEY_EXTRA + "room_number";
  public static final String EXTRA_MODEL = KEY_EXTRA + "model";
  public static final String EXTRA_IDENTITY_LIST = KEY_EXTRA + "identity_list";
  /*********************************** API ***********************************/
//  private static final String API_HOST_URL = "http://api.yuanqi.tv";
  private static final String API_HOST_URL = "http://e-hentai.org";
  private static final String API_LOGIN_HOST_URL = "http://forums.e-hentai.org";
  //  private static final String API_HOST_URL = "http://192.168.1.66:5001";
  private static final String WS_HOST_URL = "ws://api.yuanqi.tv";
  //  private static final String WS_HOST_URL = "ws://192.168.1.66:5001";
  // earth
  public static final String API_HOME_LIST = API_HOST_URL + "/p/2333";
  //business_match
  public static final String API_RECENT_MATCH = API_HOST_URL + "/user/recent_matches";
  // business_account
  public static final String API_LOGIN = API_LOGIN_HOST_URL + "/index.php?act=Login&CODE=01";
  public static final String API_REGISTER = API_HOST_URL + "/account/sms_register";
  public static final String API_VERIFY_SMS = API_HOST_URL + "/account/sms_verify";
  public static final String API_UPDATE_PASSWORD = API_HOST_URL + "/account/change_password";
  // business_user
  public static final String API_GET_USER_BY_ID = API_HOST_URL + "/user/get_by_id";
  public static final String API_USER_CURRENT = API_HOST_URL + "/user/current";
  public static final String API_UPLOAD_IMAGE = API_HOST_URL + "/upload/avatar";
  public static final String API_UPDATE_USER = API_HOST_URL + "/user/update_profile";
  // business_account_recovery
  public static final String API_RESET_PASSWORD_BY_MOBILE =
      API_HOST_URL + "/account/recovery_password_via_sms";
  // business_search
  public static final String API_SEARCH = API_HOST_URL + "/search";
  public static final String API_SEARCH_MEMBER = API_HOST_URL + "/search/user";
  // business_chat_room
  public static final String API_GET_CHAT_ROOM = API_HOST_URL + "/chatroom/get";
  public static final String API_CHAT_PUSH = WS_HOST_URL + "/chat/push/%1$s";
  public static final String API_WEB_SOCKET = WS_HOST_URL + "/ws";
  public static final String API_CHAT_HISTORY = API_HOST_URL + "/chatroom/chat_history";
  public static final String API_USER_READY = API_HOST_URL + "/chatroom/user_ready";
  public static final String API_GET_PLAY_STYLE = API_HOST_URL + "/playstyle/list";
  // business_match
  public static final String API_GET_MATCH = API_HOST_URL + "/match/get_by_id";
  public static final String API_FINISH_MATCH = API_HOST_URL + "/match/finish";
  public static final String API_START_MATCH = API_HOST_URL + "/match/start";
  public static final String API_CANCEL_MATCH = API_HOST_URL + "/match/cancel";
  // business_room
  public static final String API_CREATE_ROOM = API_HOST_URL + "/room/create";
  public static final String API_GET_ROOM = API_HOST_URL + "/room/get_by_id";
  public static final String API_JOIN_ROOM = API_HOST_URL + "/room/join";
  public static final String API_CLOSE_ROOM = API_HOST_URL + "/room/close";
  public static final String API_LEAVE_ROOM = API_HOST_URL + "/room/leave";
  public static final String API_INVITE_MEMBER = API_HOST_URL + "/room/invite";
  public static final String API_TRANS_GROUP = API_HOST_URL + "/room/trans_group";
  public static final String API_UPDATE_ROOM = API_HOST_URL + "/room/update";
  public static final String API_RECENT_MEMBER = API_HOST_URL + "/user/recent_teammates";
  public static final String API_ADD_GROUP = API_HOST_URL + "/room/add_group";
  public static final String API_SWITCH_GROUP = API_HOST_URL + "/room/switch_group";
  // business_chat_room
  public static final String API_CHAT_ROOM_LIST = API_HOST_URL + "/chatroom/list";
  public static final String API_NEW_MATCH = API_HOST_URL + "/chatroom/match";
  public static final String API_CANCEL_ARRANGE_MATCH = API_HOST_URL + "/chatroom/cancel_match";
  public static final String API_CHAT_ROOM_INVITE_USER = API_HOST_URL + "/chatroom/invite_user";
  public static final String API_CHAT_ROOM_REMOVE_USER = API_HOST_URL + "/chatroom/remove_user";
  public static final String API_UPDATE_CHAT_ROOM_INFO = API_HOST_URL + "/chatroom/update";
  // business_region
  public static final String API_GET_REGION = API_HOST_URL + "/region/list";
  // business_notification
  public static final String API_NOTIFICATION_LIST = API_HOST_URL + "/notification/list";
  // business_others
  public static final String API_FEEDBACK = "http://fb.umeng.com/api/v2/feedback/new";
  public static final String API_SET_DEVICE_ID = API_HOST_URL + "/user/set_device_id";
  // basket business
  public static final String API_REGION_RANKING = API_HOST_URL + "/rank/region";
  public static final String API_GLOBAL_RANKING = API_HOST_URL + "/rank/global";
  public static final String API_WS_ROOM = "ws://api.yuanqi.tv/push/%s";
  /******************************** Network Related ********************************/
  public static final String NETWORK_HEADER_SSID = "SSID";
  /*********************************** UM Social ***********************************/
  public static final String PLATFORM_QQ = "qq";
  public static final String PLATFORM_WEIXIN = "wechat";
  public static final String PLATFORM_WEIBO = "weibo";
  public static final String THIRD_ACCOUNT = "third_account";
  public static final String THIRD_ACCOUNT_QQ = "account_qq";
  public static final String THIRD_ACCOUNT_SINA = "account_sina";
  public static final String THIRD_ACCOUNT_WX = "account_wx";
  public static final String DESCRIPTOR = "com.umeng.share";
  public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
  public static final String APP_ID_SINA = "1490658744";
  public static final String APP_SECRET_SINA = "29c98275a96b8695c55f8b125aef8519";
  public static final String APP_ID_QQ = "1105346526";
  public static final String APP_KEY_QQ = "x9u8wC3NllIEZSDY";
  public static final String APP_ID_WX = "wx4b50e46e23ee3dd4";
  public static final String APP_SECRET_WX = "7fbeb429ea9920a0300f7a34e791bbed";
  public static final String TARGET_URL = "https://yuanqi.tv";
  /************************************ ConstValues ***********************************/
  public static final int MIN_PASSWORD_LENGTH = 6;
  public static final int MAX_PASSWORD_LENGTH = 30;
  public static final int MIN_NICKNAME_LENGTH = 2;
  public static final int MAX_NICKNAME_LENGTH = 12;
  public static final int MAX_SIGNATURE_LENGTH = 30;
  public static final int MIN_SIGNATURE_LENGTH = 0;
  public static final int PHONE_NUM_LENGTH = 11;
  public static final int MAX_LIVE_TITLE_LENGTH = 30;
  public static final int MIN_LIVE_TITLE_LENGTH = 2;
  public static final int VITALITY_PICK_IMAGE = 2333;
  public static final int VITALITY_CROP_IMAGE = 6666;
  public static final int VITALITY_CAPTURE = 9494;
  public static final int IMAGE_TYPE_AVATAR = 0;
  public static final int IMAGE_TYPE_COVER = 1;
  public static final float ALPHA_FULL = 1.0f;
  public static final float ALPHA_LITTLE = 0.7f;
  public static final int CLOSE_TYPE_DISBAND = 1;
  public static final int CLOSE_TYPE_EXIT = 2;
  public static final int MAX_STATURE = 200;
  public static final int MIN_STATURE = 155;
  public static final String GENDER_FEMALE = "女";
  public static final String STRING_SPACE = " ";
  /************************************ REGISTERWay ***********************************/
  public static final String REGISTER_WAY = "register_way";
  public static final int REGISTER_PHONE = 0;
  public static final int REGISTER_THIRD_ACCOUNT = 1;


  private Const() {}
}
