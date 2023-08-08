package com.android.speaker.server.util;

public class UrlManager {
    private static final String BASE_URL = "http://talkease.top/speakEnglish";
    public static String formatUrl(String url) {
        if(url == null || "".equals(url)) {
            return "";
        }
        return url.startsWith("http") ? url : BASE_URL + url;
    }

    /**
     * 登录
     */
    public static final String USER_LOGIN = "/speakEnglish-auth/oauth/token";

    /**
     * 获取验证码
     */
    public static final String GET_CAPTCHA = "/speakEnglish-auth/sms-code";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "/speakEnglish-ums/app-api/v1/user/userInfo";

    /**
     * 获取学习信息
     */
    public static final String GET_LEARN_INFO = "/speakEnglish-ums/app-api/v1/user/userLearnInfo";

    /**
     * 精品课程首页推荐
     */
    public static final String GET_RECOMMEND_COURSE_LIST = "/speakEnglish-ums/app-api/v1/project/recommendList";
    /**
     * 开口说分页数据
     */
    public static final String GET_OPEN_SPEAKER_LIST = "/speakEnglish-ums/app-api/v1/project/listOpenSpeak";

    /**
     * 课程主类目录
     */
    public static final String GET_CATEGORY_LIST = "/speakEnglish-ums/app-api/v1/project/listAllCategories";

    /**
     * 通过主类获取课堂信息
     */
    public static final String GET_COURSE_LIST_BY_MAIN = "/speakEnglish-ums/app-api/v1/project/listByMain";

    /**
     * 精品课程分页数据
     */
    public static final String GET_QUALITY_COURSE_LIST = "/speakEnglish-ums/app-api/v1/project/list";

    /**
     * 专项课程分页数据
     */
    public static final String GET_SPECIAL_COURSE_LIST = "/speakEnglish-ums/app-api/v1/course/specialization";

    /**
     * 通过子类获取课堂信息
     */
    public static final String GET_COURSE_LIST_BY_SUB = "/speakEnglish-ums/app-api/v1/project/listBySub";

    /**
     * 英语博客/推荐博客
     */
    public static final String GET_BLOG_LIST = "/speakEnglish-ums/app-api/v1/blog/getBlogList";

    /**
     * 场景连播
     */
    public static final String GET_SCENE_PLAY_LIST = "/speakEnglish-ums/app-api/v1/project/scenePlaylPage";

    /**
     * 节目精选
     */
    public static final String GET_FEATURED_LIST = "/speakEnglish-ums/app-api/v1/blog/getFeaturedList";

    /**
     * 热门单集（根据收藏数和点赞数进行排序）
     */
    public static final String GET_HOT_BLOG_LIST = "/speakEnglish-ums/app-api/v1/blog/getHotBlogList";
}
